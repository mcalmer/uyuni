/*
 * Copyright (c) 2021--2025 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */

package com.redhat.rhn.domain.server.ansible;

import com.redhat.rhn.common.hibernate.PathConverter;
import com.redhat.rhn.domain.BaseDomainHelper;
import com.redhat.rhn.domain.server.MinionServer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represents path to an Ansible entity (inventory, playbook).
 */
@Entity
@Table(name = "suseAnsiblePath")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class AnsiblePath extends BaseDomainHelper {

    private Long id;
    private MinionServer minionServer;
    private Path path;

    public enum Type {
        INVENTORY("inventory"),
        PLAYBOOK("playbook");

        private final String dbLabel;

        Type(String dbLabelIn) {
            this.dbLabel = dbLabelIn;
        }

        /**
         * Gets the dbLabel.
         *
         * @return dbLabel
         */
        public String getLabel() {
            return dbLabel;
        }

        /**
         * Convert from label
         * @param lbl the lable
         * @return the Type
         */
        public static Type fromLabel(String lbl) {
            for (Type value : values()) {
                if (value.dbLabel.equals(lbl)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Unsupported label: " + lbl);
        }
    }

    /**
     * Standard constructor
     */
    protected AnsiblePath() { }

    /**
     * Standard constructor
     * @param minionServerIn the minion server
     */
    protected AnsiblePath(MinionServer minionServerIn) {
        this.minionServer = minionServerIn;
    }

    /**
     * Gets the type of the Path.
     * @return the Path type
     */
    @Transient
    public abstract Type getEntityType();

    /**
     * Gets the id.
     *
     * @return id
     */
    @Id
    @GeneratedValue(generator = "ansible_path_seq")
    @GenericGenerator(
            name = "ansible_path_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "suse_ansible_path_seq"),
                    @Parameter(name = "increment_size", value = "1")
            })
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param idIn the id
     */
    public void setId(Long idIn) {
        id = idIn;
    }

    /**
     * Gets the minionServer.
     *
     * @return minionServer
     */
    @ManyToOne
    @JoinColumn(name = "server_id")
    public MinionServer getMinionServer() {
        return minionServer;
    }

    /**
     * Sets the minionServer.
     *
     * @param minionServerIn the minionServer
     */
    public void setMinionServer(MinionServer minionServerIn) {
        minionServer = minionServerIn;
    }

    /**
     * Gets the path.
     *
     * @return path
     */
    @Column
    @Convert(converter = PathConverter.class)
    public Path getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param pathIn the path
     */
    public void setPath(Path pathIn) {
        path = pathIn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", getEntityType())
                .append("minionServer", minionServer)
                .append("path", path)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnsiblePath that = (AnsiblePath) o;

        return new EqualsBuilder()
                .append(getEntityType(), that.getEntityType())
                .append(minionServer, that.minionServer)
                .append(path, that.path).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(minionServer)
                .append(path)
                .toHashCode();
    }
}
