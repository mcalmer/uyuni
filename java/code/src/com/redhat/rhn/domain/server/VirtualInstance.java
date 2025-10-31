/*
 * Copyright (c) 2009--2012 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.rhn.domain.server;

import com.redhat.rhn.domain.BaseDomainHelper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * VirtualInstance represents a virtual guest system. When the guest is
 * registered, there is an associated {@link Server} that contains additional
 * information about the guest.
 *
 * This class maps to the RHN.RhnVirtualInstance table. The schema for the
 * RhnVirtualInstance supports guests of guests; however guests of guests is not
 * being implemented in the RHN 500 release.
 *
 */
@Entity
@Table(name = "rhnVirtualInstance")
public class VirtualInstance extends BaseDomainHelper {

    private static final VirtualInstanceInfo NULL_INFO = new VirtualInstanceInfo();

    @Id
    @GeneratedValue(generator = "rhn_vi_seq")
    @GenericGenerator(
            name = "rhn_vi_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "rhn_vi_id_seq"),
                    @Parameter(name = "increment_size", value = "1")
            })
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "virtual_system_id")
    private Server guestSystem;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "host_system_id")
    private Server hostSystem;

    @Column
    private String uuid;

    @Column
    private Long confirmed;

    @OneToOne(mappedBy = "parent")
    @Cascade(CascadeType.ALL)
    private VirtualInstanceInfo info;

    /**
     * Default constructor
     */
    public VirtualInstance() {
    }

    /**
     * This constructor is for testing only.
     *
     * @param instanceId The unique id to assign to the guest
     */
    protected VirtualInstance(Long instanceId) {
        id = instanceId;
    }

    /**
     * Return the database identifier, the primary key.
     * @return The database identifier, the primary key.
     */
    public Long getId() {
        return id;
    }

    private void setId(Long virtualInstanceId) {
        id = virtualInstanceId;
    }

    /**
     * Return the system's UUID.
     * @return The system's UUID.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     *
     * @param newUuid The new UUID
     */
    public void setUuid(String newUuid) {
        uuid = newUuid;
    }

    /**
     *
     * @return confirmed
     */
    public Long getConfirmed() {
        return confirmed;
    }

    /**
     *
     * @param isConfirmed long
     */
    public void setConfirmed(Long isConfirmed) {
        confirmed = isConfirmed;
    }

    /**
     * When the virtual instance is registered, this method returns a Server
     * corresponding to the guest that this virtual instance represents.
     *
     * @return A Server object corresponding to the guest that this virtual
     * instance represents when the guest is registered; otherwise,
     * <code>null</code> is returned
     */
    public Server getGuestSystem() {
        return guestSystem;
    }

    /**
     * Sets the Server corresponding to the guest represented by this virtual
     * instance.
     * @param system the guest system
     */
    public void setGuestSystem(Server system) {
        this.guestSystem = system;

        if (guestSystem != null) {
            guestSystem.setVirtualInstance(this);
        }
    }

    /**
     * Returns a guest's parent or host system.
     *
     * <strong>Note:</strong> For the RHN 500 release, the Server returned from
     * this method will always be a non-virtual system. The underlying database
     * schema is designed to support guests of guests, but we are not
     * implementing guests of guests for the 500 release. With guests of guests,
     * this method could return a virtual guest as well.
     *
     * @return A Server object that represents the actual host
     */
    public Server getHostSystem() {
        return hostSystem;
    }

    /**
     * Sets the parent/host for this guest.
     *
     * @param system The host system
     */
    public void setHostSystem(Server system) {
        hostSystem = system;
    }

    /**
     * Returns <code>true</code> if this virtual instance represents a
     * registered guest.
     *
     * @return <code>true</code> if this virtual instance represents a
     * registered guest, <code>false</code> otherwise.
     */
    public boolean isRegisteredGuest() {
        return getGuestSystem() != null;
    }

    /**
     * the info
     * @return the info
     */
    private VirtualInstanceInfo getInstanceInfo() {
        return info;
    }

    /**
     * set the info
     * @param instanceInfo the info
     */
    // TODO Determine if we need to handle deletion of the info.
    // If there are use cases for deleting the VirtualInstanceInfo from a
    // VirtualInstance
    // object, then this method will need to be refactored to handle deletion.
    private void setInstanceInfo(VirtualInstanceInfo instanceInfo) {
        info = instanceInfo;

        if (info != null) {
            info.setParent(this);
        }
    }

    private VirtualInstanceInfo getInfo() {
        if (info == null) {
            return NULL_INFO;
        }

        return info;
    }

    private VirtualInstanceInfo initInfo() {
        if (info == null) {
            VirtualInstanceType pv =
                VirtualInstanceFactory.getInstance().getFullyVirtType();
            info = new VirtualInstanceInfo();
            info.setParent(this);
            info.setType(pv);
        }

        return info;
    }

    /**
     * Returns the name of the virtual instance.
     *
     * @return The name of the virtual instance
     */
    public String getName() {
        return getInfo().getName();
    }

    /**
     * Set the name of the virtual instance.
     *
     * @param name The new name
     */
    public void setName(String name) {
        initInfo().setName(name);
    }

    /**
     * Returns the total memory in MiB allocated to the virtual instance.
     *
     * @return The total memory in MiB allocated to the virtual instance.
     */
    public Long getTotalMemory() {
        return getInfo().getTotalMemory();
    }

    /**
     * Sets the total memory in MiB allocated to the virtual instance.
     *
     * @param memory The total memory in MiB
     */
    public void setTotalMemory(Long memory) {
        initInfo().setTotalMemory(memory);
    }

    /**
     * Returns the number of CPUs allocated to the virtual instance.
     *
     * @return The number of CPUs allocated to the virtual instance
     */
    public Integer getNumberOfCPUs() {
        return getInfo().getNumberOfCPUs();
    }

    /**
     * Sets the number of CPUs allocated to the virtual instance.
     *
     * @param number The number of CPUs
     */
    public void setNumberOfCPUs(Integer number) {
        initInfo().setNumberOfCPUs(number);
    }

    /**
     * Returns the virtualization type for this instance.
     *
     * @return The virtualization type for this instance.
     */
    public VirtualInstanceType getType() {
        return getInfo().getType();
    }

    /**
     * Sets the virtualization type for this instance.
     *
     * @param type The new virtualization type
     */
    public void setType(VirtualInstanceType type) {
        initInfo().setType(type);
    }

    /**
     * Returns the state of the virtual instance.
     *
     * @return The state of the virtual instance.
     */
    public VirtualInstanceState getState() {
        return getInfo().getState();
    }

    /**
     * Sets the state of the virtual instance.
     *
     * @param state The new state
     */
    public void setState(VirtualInstanceState state) {
        initInfo().setState(state);
    }

    /**
     * Two virtual instancess are considered equal when they share the same
     * UUID.
     *
     * @param object The object to test against
     *
     * @return <code>true</code> if <code>object</code> is a VirtualInstance
     * and has the same uuid as this VirtualInstance, <code>false</code>
     * otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof VirtualInstance that)) {
            return false;
        }
        return new EqualsBuilder()
                .append(this.getUuid(), that.getUuid())
                .append(this.getHostSystem(), that.getHostSystem())
                .isEquals();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getUuid())
                .append(getHostSystem())
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("uuid", uuid)
                .toString();
    }

    /**
     * An adapter method that transforms the virtual instance into a view.
     *
     * @return A GuestAndNonVirtHostView
     */
    public GuestAndNonVirtHostView asGuestAndNonVirtHostView() {
        if (hostSystem == null) {
            return new GuestAndNonVirtHostView(guestSystem.getId(), guestSystem.getOrg().getId(),
                    guestSystem.getName());
        }
        return new GuestAndNonVirtHostView(guestSystem.getId(), guestSystem.getOrg().getId(),
                guestSystem.getName(), hostSystem.getOrg().getId(), hostSystem.getId(), hostSystem.getName());
    }

}
