/*
 * Copyright (c) 2023--2025 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */

package com.redhat.rhn.domain.credentials;

import com.redhat.rhn.domain.BaseDomainHelper;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.domain.user.legacy.UserImpl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Credentials - Java representation of the table SUSECREDENTIALS.
 *
 * This table contains pairs of credentials used for communicating
 * with 3rd party systems, e.g. API usernames and keys.
 */
@Entity
@Table(name = "suseCredentials")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@NamedNativeQuery(
    name = "BaseCredentials.lookupByPaygSSHDataId",
    query = "SELECT sc.* FROM suseCredentials sc WHERE sc.payg_ssh_data_id = :sshDataId",
    resultClass = BaseCredentials.class
)
@NamedNativeQuery(
    name = "BaseCredentials.lookupByPaygSSHDataHostname",
    query = "SELECT sc.* " +
        "FROM suseCredentials sc INNER JOIN susepaygsshdata sd ON sc.payg_ssh_data_id = sd.id " +
        "WHERE sd.host = :hostname",
    resultClass = BaseCredentials.class
)
@NamedQuery(
    name = "BaseCredentials.getLastSCCRefreshDate",
    query = "SELECT MAX(modified) FROM BaseCredentials WHERE type IN ('scc', 'cloudrmt')"
)
public abstract class BaseCredentials extends BaseDomainHelper implements Credentials {

    private Long id;
    private User user;

    /**
     * Get the ID of this object.
     * @return id
     */
    @Override
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "suse_credentials_seq")
    @GenericGenerator(
            name = "suse_credentials_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "suse_credentials_id_seq"),
                    @Parameter(name = "increment_size", value = "1")
            })
    public Long getId() {
        return this.id;
    }

    /**
     * Set the ID of this object.
     * @param idIn id
     */
    @Override
    public void setId(Long idIn) {
        this.id = idIn;
    }

    /**
     * Get the associated {@link User}.
     * @return user
     */
    @Override
    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return this.user;
    }

    /**
     * Set the associated {@link User}.
     * @param userIn user
     */
    @Override
    public void setUser(User userIn) {
        this.user = userIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BaseCredentials that)) {
            return false;
        }

        return new EqualsBuilder()
            .append(user, that.user)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(user)
            .toHashCode();
    }
}
