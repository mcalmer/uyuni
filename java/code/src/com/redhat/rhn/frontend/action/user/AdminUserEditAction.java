/*
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
package com.redhat.rhn.frontend.action.user;

import com.redhat.rhn.GlobalInstanceHolder;
import com.redhat.rhn.common.security.PermissionException;
import com.redhat.rhn.common.util.StringUtil;
import com.redhat.rhn.domain.access.AccessGroup;
import com.redhat.rhn.domain.org.Org;
import com.redhat.rhn.domain.role.Role;
import com.redhat.rhn.domain.role.RoleFactory;
import com.redhat.rhn.domain.server.ManagedServerGroup;
import com.redhat.rhn.domain.server.ServerGroup;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.frontend.action.common.BadParameterException;
import com.redhat.rhn.frontend.struts.RequestContext;
import com.redhat.rhn.frontend.struts.RhnHelper;
import com.redhat.rhn.frontend.struts.StrutsDelegate;
import com.redhat.rhn.manager.access.AccessGroupManager;
import com.redhat.rhn.manager.user.UserManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UserEditSubmitAction, edit action submit handler for user detail page
 */
public class AdminUserEditAction extends UserEditActionHelper {

    private static Logger log = LogManager.getLogger(AdminUserEditAction.class);
    private static final AccessGroupManager ACCESS_GROUP_MANAGER = GlobalInstanceHolder.ACCESS_GROUP_MANAGER;
    private static final String ROLE_SETTING_PREFIX = "role_";

    /** {@inheritDoc} */
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm formIn,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm)formIn;

        RequestContext requestContext = new RequestContext(request);
        StrutsDelegate strutsDelegate = getStrutsDelegate();

        //We could be editing ourself, we could be editing another user...
        User targetUser = UserManager.lookupUser(requestContext.getCurrentUser(),
                                           requestContext.getParamAsLong("uid"));
        request.setAttribute(RhnHelper.TARGET_USER, targetUser);

        //Make sure we got a user, if not, we must have gotten a bad uid
        if (targetUser == null) {
            throw new BadParameterException("Invalid uid, targetUser not found");
        }
        User loggedInUser = requestContext.getCurrentUser();

        //Update the users details with info entered on the form
        ActionErrors errors = updateDetails(loggedInUser, targetUser, form);
        //If we have validation/form errors, return now and let the user fix those first
        if (!errors.isEmpty()) {
            return returnFailure(mapping, request, errors, targetUser.getId());
        }

        //Create the user info updated success message
        createSuccessMessage(request, "message.userInfoUpdated", null);

        //Now we need to update user roles. If we get errors here, return failure
        errors = updateRoles(request, targetUser, loggedInUser);
        if (!errors.isEmpty()) {
            return returnFailure(mapping, request, errors, targetUser.getId());
        }

        //Everything must have gone smoothly
        UserManager.storeUser(targetUser);

        ActionForward dest = mapping.findForward("success");
        /*
         * Does the user still have the roles needed to see /users/UserDetails.do?
         * Check here and make a decision so user doesn't go to a permission error page.
         */
        //If the logged in user is the same as the target user and we have removed the
        //target users org admin status, forward to noaccess instead
        if (loggedInUser.equals(targetUser) &&
            !targetUser.hasRole(RoleFactory.ORG_ADMIN)) {
            dest = mapping.findForward("noaccess");
        }

        return strutsDelegate.forwardParam(dest, "uid", String.valueOf(targetUser.getId()));
    }

    /**
     * Private helper method to save errors to the request and forward to the
     * failure mapping
     */
    private ActionForward returnFailure(ActionMapping mapping,
                                        HttpServletRequest request,
                                        ActionErrors errors,
                                        Long uid) {
        addErrors(request, errors);
        return getStrutsDelegate().forwardParam(mapping.findForward("failure"), "uid",
                                      String.valueOf(uid));
    }

    /**
     * Private helper method to handle getting the new roles from the form and calling
     * UserManager.updateUserRolesFromRoleLabels().
     * @param form The form containing selectedRoles. selectedRoles are the new set of
     *             roles to associate with the user.
     * @param targetUser The user who is having their roles updated.
     * @return Returns an ActionErrors object containing any errors that occurred while
     *         updating the users roles
     */
    private ActionErrors updateRoles(HttpServletRequest request,
                                        User targetUser,
                                        User loggedInUser) {
        log.debug("{}.updateRoles()", this.getClass().getName());

        Set<String> disabledRoles = extractDisabledRoles(request);

        ActionErrors errors = new ActionErrors();
        Org org = targetUser.getOrg();
        Set<Role> orgRoles = org.getRoles();

        // Build a set of the users current role labels to help determine what we need
        // to add and remove:
        Set<String> existingRoles = targetUser.getPermanentRoles().stream()
                .map(r -> r.getLabel())
                .collect(Collectors.toSet());

        // Look for an add/remove setting for each org role in the form:
        List<String> rolesToAdd = new LinkedList<>();
        List<String> rolesToRemove = new LinkedList<>();
        for (Role role : orgRoles) {

            if (disabledRoles.contains(role.getLabel())) {
                // Role was disabled when we built this form, so skip:
                continue;
            }

            String roleSetting = request.getParameter(ROLE_SETTING_PREFIX + role.getLabel());
            if (log.isDebugEnabled()) {
                log.debug("   {} / {}", role.getName(), StringUtil.sanitizeLogInput(roleSetting));
            }

            if (roleSetting != null && !existingRoles.contains(role.getLabel())) {
                // Must have been newly checked:
                rolesToAdd.add(role.getLabel());
            }
            else if (roleSetting == null && existingRoles.contains(role.getLabel())) {
                // Must have been newly unchecked:
                rolesToRemove.add(role.getLabel());
            }
        }

        try {
            processRBACGroupAssignments(request, targetUser);
            processAdminRoleAssignments(request, rolesToAdd, rolesToRemove, targetUser, loggedInUser);
        }
        catch (PermissionException pe) {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("userdetails.jsp.error.lastorgadmin"));
        }

        return errors;
    }

    private void processAdminRoleAssignments(HttpServletRequest request, List<String> rolesToAdd,
                                             List<String> rolesToRemove, User target, User loggedInUser) {

        Predicate<String> onlyAdmin =
                (String r) -> RoleFactory.ORG_ADMIN.getLabel().equals(r) || RoleFactory.SAT_ADMIN.getLabel().equals(r);

        UserManager.addRemoveUserRoles(target, rolesToAdd.stream().filter(onlyAdmin).toList(),
                rolesToRemove.stream().filter(onlyAdmin).toList());

        //if he is an org amin make sure he does NOT
        // have any subscribed Server Groups, because
        // by becoming an org admin he is automatically
        // subscribed to every group... and so his list
        // will be empty..
        if (target.hasRole(RoleFactory.ORG_ADMIN) &&
                !target.getAssociatedServerGroups().isEmpty()) {
            Set<User> admins = new HashSet<>();
            admins.add(target);
            for (Iterator<ServerGroup> itr = target.getAssociatedServerGroups().iterator(); itr.hasNext();) {
                ManagedServerGroup sg = (ManagedServerGroup) itr.next();
                GlobalInstanceHolder.SERVER_GROUP_MANAGER.dissociateAdmins(sg, admins, loggedInUser);
                itr.remove();
            }
        }
    }

    private void processRBACGroupAssignments(HttpServletRequest request, User target) {
        var userGroups = target.getAccessGroups();
        var currentGroupLabels = userGroups.stream()
                .map(AccessGroup::getLabel)
                .collect(Collectors.toUnmodifiableSet());

        Iterator<AccessGroup> iterator = userGroups.iterator();
        while (iterator.hasNext()) {
            AccessGroup group = iterator.next();
            String groupSetting = request.getParameter(ROLE_SETTING_PREFIX + group.getLabel());

            if (groupSetting == null) {
                iterator.remove();
            }
        }

        for (AccessGroup group : ACCESS_GROUP_MANAGER.list(target.getOrg())) {
            String groupSetting = request.getParameter(ROLE_SETTING_PREFIX + group.getLabel());
            if (groupSetting != null && !currentGroupLabels.contains(group.getLabel())) {
                userGroups.add(group);
            }
        }
    }

    private Set<String> extractDisabledRoles(HttpServletRequest request) {
        String hiddenInput = request.getParameter("disabledRoles");
        Set<String> returnVal = new HashSet<>(
                Arrays.asList(hiddenInput.split("\\|")));
        log.debug("Found disabled inputs: {}", returnVal);
        return returnVal;
    }
}
