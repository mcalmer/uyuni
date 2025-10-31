/*
 * Copyright (c) 2025 SUSE LLC
 * Copyright (c) 2009--2014 Red Hat, Inc.
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
package com.redhat.rhn.domain.channel;

import com.redhat.rhn.domain.BaseDomainHelper;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ChannelProduct - Class representation of the table rhnChannelProduct.
 */
@Entity
@Table(name = "rhnChannelProduct")
public class ChannelProduct extends BaseDomainHelper {

    @Id
    @GeneratedValue(generator = "channelprod_seq")
    @GenericGenerator(
            name = "channelprod_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "rhn_channelprod_id_seq"),
                    @Parameter(name = "increment_size", value = "1")
            })
    private Long id;

    @Column
    private String product;

    @Column
    private String version;

    @Column(name = "beta")
    private String betaMarker;

    /**
     * Getter for id
     * @return Long to get
    */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for id
     * @param idIn to set
    */
    protected void setId(Long idIn) {
        this.id = idIn;
    }

    /**
     * Getter for product
     * @return String to get
    */
    public String getProduct() {
        return this.product;
    }

    /**
     * Setter for product
     * @param productIn to set
    */
    public void setProduct(String productIn) {
        this.product = productIn;
    }

    /**
     * Getter for version
     * @return String to get
    */
    public String getVersion() {
        return this.version;
    }

    /**
     * Setter for version
     * @param versionIn to set
    */
    public void setVersion(String versionIn) {
        this.version = versionIn;
    }

    /**
     * Getter for betaMarker
     * @return String to get
    */
    private String getBetaMarker() {
        return this.betaMarker;
    }

    /**
     * Setter for betaMarker
     * @param betaIn to set
    */
    private void setBetaMarker(String betaIn) {
        this.betaMarker = betaIn;
    }

    /**
     * Whether the channel product is a betaMarker product
     * @return true if product is a betaMarker product, false otherwise
     */
    public boolean isBeta() {
        return this.betaMarker.equals("Y");
    }

    /**
     * Setter for whether the channel product is a betaMarker product or not
     * Can't be named setBetaMarker or hibernate wigs out
     * @param isBeta true if the product is a betaMarker product, false otherwise
     */
    public void setBeta(boolean isBeta) {
        if (isBeta) {
            this.setBetaMarker("Y");
        }
        else {
            this.setBetaMarker("N");
        }
    }
}
