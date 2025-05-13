package com.example.miniapp.services.strategy;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;

public interface DeliveryStrategy {
    void deliver(UserNotification userNotification, Notification request);
}