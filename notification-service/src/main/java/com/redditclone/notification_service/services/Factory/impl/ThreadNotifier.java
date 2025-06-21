package com.redditclone.notification_service.services.Factory.impl;

import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ThreadNotifier extends Notification {
    private UUID threadOwnerId;
    private String threadOwnerMessage;

    public ThreadNotifier() {
        super();
    }

    public ThreadNotifier(String senderId, String title, String message, String senderName, List<UUID> receiversId) {
        super(NotificationType.THREAD, senderId, title, message, senderName, receiversId);
        this.threadOwnerId = receiversId.getFirst();
        this.threadOwnerMessage = "Hello thread owner, you have a new notification!";

        List<UUID> followers;
        if (receiversId.size() > 1) {
            followers = new ArrayList<>(receiversId.subList(1, receiversId.size()));
        } else {
            followers = Collections.emptyList();
        }
        this.setReceiversId(followers);
    }
}