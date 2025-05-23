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
package com.redhat.rhn.frontend.action;

import com.redhat.rhn.GlobalInstanceHolder;
import com.redhat.rhn.domain.server.Server;
import com.redhat.rhn.domain.server.ServerFactory;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.frontend.struts.RequestContext;
import com.redhat.rhn.frontend.struts.RhnAction;
import com.redhat.rhn.frontend.struts.RhnHelper;

import com.suse.cloud.CloudPaygManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Simple generic Index Action

/**
 * IndexAction extends RhnAction
 */
public class GenericIndexAction extends RhnAction {

    private static final CloudPaygManager CLOUD_PAYG_MANAGER = GlobalInstanceHolder.PAYG_MANAGER;

    /** {@inheritDoc} */
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm formIn,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        RequestContext rctx = new RequestContext(request);
        User user = rctx.getCurrentUser();

        if (CLOUD_PAYG_MANAGER.isPaygInstance()) {
            CLOUD_PAYG_MANAGER.checkRefreshCache(true);
            if (!CLOUD_PAYG_MANAGER.hasSCCCredentials()) {
                String nonCompliantServers = getNonCompliantByosInPayg(user);
                if (StringUtils.isNotEmpty(nonCompliantServers)) {
                    createErrorMessage(request, "message.payg.errorbyosnosccssm", null);
                }
            }
        }

        return mapping.findForward(RhnHelper.DEFAULT_FORWARD);
    }

    private String getNonCompliantByosInPayg(User user) {
            List<Server> servers = ServerFactory.listSystemsInSsm(user);
            return servers.stream()
                    .filter(s -> s.isDeniedOnPayg())
                    .map(Server::getName)
                    .collect(Collectors.joining(","));
    }
}
