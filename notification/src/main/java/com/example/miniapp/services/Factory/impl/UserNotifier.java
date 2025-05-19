package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class UserNotifier extends Notification {
    private UUID receiverId;
    public UserNotifier() { super(); }
    public UserNotifier(String senderId, String title, String message, String senderName, UUID receiverId) {
        super(NotificationType.USER_SPECIFIC, senderId, title, message, senderName, null);
        this.receiverId = receiverId;
    }
}