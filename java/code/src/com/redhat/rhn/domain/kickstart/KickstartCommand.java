/*
 * Copyright (c) 2009--2017 Red Hat, Inc.
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
package com.redhat.rhn.domain.kickstart;

import com.redhat.rhn.domain.BaseDomainHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * KickstartCommandName
 */
@Entity
@Table(name = "rhnKickstartCommand")
public class KickstartCommand extends BaseDomainHelper implements Comparable<KickstartCommand> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RHN_KSCOMMAND_ID_SEQ")
    @SequenceGenerator(name = "RHN_KSCOMMAND_ID_SEQ", sequenceName = "RHN_KSCOMMAND_ID_SEQ", allocationSize = 1)
    private Long id;

    @Column
    private String arguments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ks_command_name_id")
    private KickstartCommandName commandName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kickstart_id")
    private KickstartData kickstartData;

    @Column(name = "custom_position")
    private Integer customPosition;

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param i The id to set.
     */
    protected void setId(Long i) {
        this.id = i;
    }

    /**
     * @return Returns the name.
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * @param argsIn The arguments to set.
     */
    public void setArguments(String argsIn) {
        this.arguments = argsIn;
    }

    /**
     * @return Returns the ksdata.
     */
    public KickstartData getKickstartData() {
        return kickstartData;
    }

    /**
     * @param ksdataIn The KickstartData to set.
     */
    public void setKickstartData(KickstartData ksdataIn) {
        this.kickstartData = ksdataIn;
    }

    /**
     * @return Returns the kickstart command name.
     */
    public KickstartCommandName getCommandName() {
        return commandName;
    }

    /**
     * @param commandNameIn The KickstartData to set.
     */
    public void setCommandName(KickstartCommandName commandNameIn) {
        this.commandName = commandNameIn;
    }

    /**
     *
     * @param k KickstartCommand to compare
     * @return how does it stack up!
     */
    @Override
    public int compareTo(KickstartCommand k) {
        if (k == this) {
            return 0;
        }
        int order = getCommandName().getOrder().compareTo(k.getCommandName().getOrder());
        if (k.getCommandName().getName().equals("custom") && getCustomPosition() != null) {
            order = getCustomPosition().compareTo(k.getCustomPosition());
            return order;
        }

        if (order == 0) {
            String ourArgs = StringUtils.defaultString(getArguments(), "");
            String theirArgs = StringUtils.defaultString(k.getArguments(), "");
            order = ourArgs.compareTo(theirArgs);
        }

        return order;
    }

    /**
     * Clone or 'deepCopy' this KickstartCommand into a new one
     * @param ksDataIn who owns this new instance
     * @return KickstartCommand object that is new.
     */
    public KickstartCommand deepCopy(KickstartData ksDataIn) {
        KickstartCommand cloned = new KickstartCommand();
        cloned.setArguments(this.getArguments());
        cloned.setCommandName(this.getCommandName());
        cloned.setKickstartData(ksDataIn);
        cloned.setCustomPosition(this.getCustomPosition());
        Date now = new Date();
        cloned.setCreated(now);
        cloned.setModified(now);
        return cloned;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
            return this.getClass().getName() + " name: " +
                this.getCommandName().getName() + " arguments " + getArguments();
     }

    /**
     * gets the custom command position
     * @return the position of the custom option
     */
    public Integer getCustomPosition() {
        return customPosition;
    }

    /**
     * sets the custom command position.  This is ignored by KickstartCommandComparator
     *          if id is not null
     * @param customPositionIn the position to set the custom option for
     */
    public void setCustomPosition(Integer customPositionIn) {
        this.customPosition = customPositionIn;
    }
}
