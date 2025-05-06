package com.example.miniapp.services.strategy;

import com.example.miniapp.models.dto.NotificationRequest;

public interface DeliveryStrategy {
    void deliver(NotificationRequest request);
}