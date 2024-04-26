package com.redhat.rhn.frontend.xmlrpc.serializer;

import com.redhat.rhn.domain.org.usergroup.UserExtGroup;
import com.redhat.rhn.domain.role.Role;
import com.redhat.rhn.domain.notification.UserNotification;

import com.suse.manager.api.ApiResponseSerializer;
import com.suse.manager.api.SerializationBuilder;
import com.suse.manager.api.SerializedApiResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * UserNotificationSerializer
 *
 * @apidoc.doc
 *  #struct_begin("notification")
 *      #prop("int", "id")
 *      #prop("int", "messageId")
 *      #prop("boolean", "read")
 *      #prop("string", "message")
 *      #prop("string", "type")
 *      #prop($date, "created")
 *  #struct_end()
 *
 */
public class UserNotificationSerializer extends ApiResponseSerializer<UserNotification> {

    @Override
    public Class<UserNotification> getSupportedClass() {
        return UserNotification.class;
    }

    @Override
    public SerializedApiResponse serialize(UserNotification src) {
        return new SerializationBuilder()
                .add("id", src.getId())
                .add("messageId", src.getMessage().getId())
                .add("read", src.getRead())
                .add("message", src.getMessage().getData())
                .add("type", src.getMessage().getType())
                .add("created", src.getMessage().getCreated())
                .build();
    }
}