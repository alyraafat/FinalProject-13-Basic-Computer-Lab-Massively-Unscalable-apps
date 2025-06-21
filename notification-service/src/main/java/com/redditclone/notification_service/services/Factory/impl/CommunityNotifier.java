package com.redditclone.notification_service.services.Factory.impl;

import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommunityNotifier extends Notification {
    public CommunityNotifier() {
        super();
    }

    public CommunityNotifier(String senderId, String title, String message, String senderName, List<UUID> receiversId) {
        super(NotificationType.COMMUNITY, senderId, title, message, senderName, receiversId);
    }
}