package com.redhat.rhn.frontend.xmlrpc.user;

import com.redhat.rhn.frontend.xmlrpc.BaseHandler;
import com.redhat.rhn.domain.notification.UserNotification;
import com.redhat.rhn.domain.notification.UserNotificationFactory;
import com.redhat.rhn.domain.user.User;

import com.suse.manager.api.ReadOnly;

import java.util.List;
import java.util.Collection;

public class UserNotificationsHandler extends BaseHandler {

    /**
     * @param user The current user
     * @param unread Unread notifications
     * @return Returns a list of notifications
     * @apidoc.doc Get all notifications from a user.
     * @apidoc.param #session_key()
     * @apidoc.param #param_desc("boolean", "unread", "Read notifications.")
     * @apidoc.returntype
     * #return_array_begin()
     *     $UserNotificationSerializer
     * #array_end()
     */
    @ReadOnly
    public List<UserNotification> getNotifications(User user, boolean unread) {
        List<UserNotification> notifications;

        if (unread) {
            notifications = UserNotificationFactory.listUnreadByUser(user);
        } else {
            notifications = UserNotificationFactory.listAllByUser(user);
        }
        return notifications;
    }

    /**
     * @param userNotification The notification
     * @return Returns 1 if successful
     * @apidoc.doc Makes a notification raed
     * @apidoc.param #param_desc("userNotification", "notification", "The target notification.")
     * @apidoc.param #session_key()
     * @apidoc.returntype #return_int_success()
     */
    public int makeNotificationsRead(User user, Collection<Integer> notifications) {
        for (UserNotification notification : getNotifications(user, true)) {
            if (notifications.contains(notification.getId())) {
                UserNotificationFactory.updateStatus(notification, true);
            }
        }
        return 1;
    }

    /**
     * @param user The current user
     * @return Returns 1 if successful
     * @apidoc.doc Makes all notifications from a user read
     * @apidoc.param #session_key()
     * @apidoc.returntype #return_int_success()
     */
    public int makeAllNotificationsRead(User user) {
        for (UserNotification notification : getNotifications(user, true)) {
            UserNotificationFactory.updateStatus(notification, true);
        }
        return 1;
    }

    /**
     * @param notifications The notifications to delete
     * @return int number of deleted notifications
     * @apidoc.doc Deletes multiple notifications
     * @apidoc.param #param_desc("collection", "notifications", "List of notifications.")
     * @apidoc.param #session_key()
     * @apidoc.returntype #return_int_success()
     */
    public int deleteNotification(User user, Collection<UserNotification> notifications) {
        UserNotificationFactory.delete(notifications);
        return 1;
    }

}