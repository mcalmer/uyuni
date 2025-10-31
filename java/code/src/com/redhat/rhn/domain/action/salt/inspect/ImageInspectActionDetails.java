/*
 * Copyright (c) 2016--2025 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */
package com.redhat.rhn.domain.action.salt.inspect;

import com.redhat.rhn.domain.BaseDomainHelper;
import com.redhat.rhn.domain.action.Action;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ImageInspectActionDetails - Class representation of the table rhnActionImageInspect.
 */
@Entity
@Table(name = "rhnActionImageInspect")
public class ImageInspectActionDetails extends BaseDomainHelper {

    @Id
    @GeneratedValue(generator = "image_inspect_seq")
    @GenericGenerator(
            name = "image_inspect_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "RHN_ACT_IMAGE_INSPECT_ID_SEQ"),
                    @Parameter(name = "increment_size", value = "1")
            })
    private Long id;

    @Column
    private String name;

    @Column
    private String version;

    @Column(name = "image_store_id")
    private Long imageStoreId;

    @OneToMany(mappedBy = "id.actionImageInspectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImageInspectActionResult> results = new HashSet<>();

    @Column(name = "build_action_id")
    private Long buildActionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "action_id", nullable = false)
    private Action parentAction;

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param versionIn the version
     */
    public void setVersion(String versionIn) {
        this.version = versionIn;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param nameIn the name in
     */
    public void setName(String nameIn) {
        this.name = nameIn;
    }

    /**
     * @return the image store id
     */
    public Long getImageStoreId() {
        return imageStoreId;
    }

    /**
     * @param imageStoreIdIn the image store id to set
     */
    public void setImageStoreId(Long imageStoreIdIn) {
        this.imageStoreId = imageStoreIdIn;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param idIn the id to set
     */
    public void setId(Long idIn) {
        this.id = idIn;
    }

    /**
     * @return the build action id
     */
    public Long getBuildActionId() {
        return buildActionId;
    }

    /**
     * @param buildActionIdIn the action id to set
     */
    public void setBuildActionId(Long buildActionIdIn) {
        this.buildActionId = buildActionIdIn;
    }

    /**
     * @return the results
     */
    public Set<ImageInspectActionResult> getResults() {
        return results;
    }

    /**
     * @param resultsIn the results to set
     */
    public void setResults(Set<ImageInspectActionResult> resultsIn) {
        this.results = resultsIn;
    }

    /**
     * Add {@link com.redhat.rhn.domain.action.salt.ApplyStatesActionResult} to the results
     * set.
     *
     * @param resultIn ApplyStatesActionResult to add to the set
     */
    public void addResult(ImageInspectActionResult resultIn) {
        resultIn.setParentScriptActionDetails(this);
        results.add(resultIn);
    }

    /**
     * Gets the parent Action associated with this ServerAction record
     * @return Returns the parentAction.
     */
    public Action getParentAction() {
        return parentAction;
    }

    /**
     * Sets the parent Action associated with this ServerAction record
     * @param parentActionIn The parentAction to set.
     */
    public void setParentAction(Action parentActionIn) {
        this.parentAction = parentActionIn;
    }
}
