/*
 * Copyright (c) 2025 SUSE LLC
 * Copyright (c) 2009--2010 Red Hat, Inc.
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
package com.redhat.rhn.domain.common;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Checksum
 * Class for checksums of files, packages and erratas.
 */
@Entity
@Table(name = "rhnChecksum")
public class Checksum {

    @Id
    @GeneratedValue(generator = "rhn_checksum_seq")
    @GenericGenerator(
            name = "rhn_checksum_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "rhnChecksum_seq"),
                    @Parameter(name = "increment_size", value = "1")
            })
    private Long id;

    @Column
    private String checksum;

    @ManyToOne
    @JoinColumn(name = "checksum_type_id")
    private ChecksumType checksumType;

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param idIn The id to set.
     */
    public void setId(Long idIn) {
        this.id = idIn;
    }

    /**
     * @return Returns the checksum.
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * @param checksumIn The checksum to set.
     */
    public void setChecksum(String checksumIn) {
        this.checksum = checksumIn;
    }

    /**
     * @return Returns the type of checksum.
     */
    public ChecksumType getChecksumType() {
        return checksumType;
    }

    /**
     * @param checksumTypeIn The checksum type to set.
     */
    public void setChecksumType(ChecksumType checksumTypeIn) {
        this.checksumType = checksumTypeIn;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Checksum other = (Checksum) o;

        return Objects.equals(id, other.id);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return checksumType + ":" + checksum;
    }
}
