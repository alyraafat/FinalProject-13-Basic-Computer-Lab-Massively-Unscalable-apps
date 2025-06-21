package com.redditclone.notification_service.services.Factory.impl;

import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class UserNotifier extends Notification {
    private UUID receiverId;

    public UserNotifier() {
        super();
    }

    public UserNotifier(String senderId, String title, String message, String senderName, UUID receiverId) {
        super(NotificationType.USER_SPECIFIC, senderId, title, message, senderName, null);
        this.receiverId = receiverId;
    }
}