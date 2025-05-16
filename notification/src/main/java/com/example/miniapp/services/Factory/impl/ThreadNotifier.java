package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.NotificationType;


import java.util.List;
import java.util.UUID;

public class ThreadNotifier extends Notification {
    public ThreadNotifier() { super(); }
    public ThreadNotifier(String senderId, String title, String message, String senderName, List<UUID> receiversId) {
        super(NotificationType.THREAD, senderId, title, message, senderName, receiversId);
    }
}