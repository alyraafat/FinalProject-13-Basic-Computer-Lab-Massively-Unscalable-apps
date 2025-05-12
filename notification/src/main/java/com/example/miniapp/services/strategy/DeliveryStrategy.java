package com.example.miniapp.services.strategy;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;

public interface DeliveryStrategy {
    void deliver(Notification request);
}