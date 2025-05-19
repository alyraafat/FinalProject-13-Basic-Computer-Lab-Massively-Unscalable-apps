package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.NotificationType;
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
    public ThreadNotifier() { super(); }
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