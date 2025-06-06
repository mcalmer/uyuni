/*
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
package com.redhat.rhn.frontend.action.configuration.test;

import com.redhat.rhn.domain.access.AccessGroupFactory;
import com.redhat.rhn.domain.rhnset.RhnSet;
import com.redhat.rhn.domain.server.test.ServerFactoryTest;
import com.redhat.rhn.frontend.dto.ConfigSystemDto;
import com.redhat.rhn.frontend.struts.RequestContext;
import com.redhat.rhn.manager.configuration.ConfigurationManager;
import com.redhat.rhn.manager.rhnset.RhnSetDecl;
import com.redhat.rhn.manager.rhnset.RhnSetManager;
import com.redhat.rhn.testing.RhnMockStrutsTestCase;
import com.redhat.rhn.testing.UserTestUtils;

import org.junit.jupiter.api.Test;

/**
 * SummaryActionTest
 */
public class SummaryActionTest extends RhnMockStrutsTestCase {

    @Test
    public void testExecute() {
        UserTestUtils.addAccessGroup(user, AccessGroupFactory.CONFIG_ADMIN);

        //The information for this page is simply stored into a set
        Long sid = ServerFactoryTest.createTestServer(user, true).getId();

        RhnSet set = RhnSetDecl.CONFIG_ENABLE_SYSTEMS.create(user);
        set.addElement(sid, (long) ConfigurationManager.ENABLE_SUCCESS);
        RhnSetManager.store(set);

        setRequestPathInfo("/configuration/system/Summary");
        actionPerform();
        verifyList(RequestContext.PAGE_LIST, ConfigSystemDto.class);
    }
}
