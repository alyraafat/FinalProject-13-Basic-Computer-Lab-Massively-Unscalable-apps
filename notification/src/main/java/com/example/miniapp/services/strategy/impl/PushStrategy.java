package com.example.miniapp.services.strategy.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.services.NotificationService;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PushStrategy implements DeliveryStrategy {
    private static final Logger logger = LoggerFactory.getLogger(PushStrategy.class);

    private final NotificationService notificationService;

    public PushStrategy(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void deliver(Notification request) {
        try {
            String formattedPayload = formatPushNotification(request);

            boolean delivered = notificationService.sendPush(formattedPayload);

            if (!delivered) {
                logger.warn("Push delivery failed for request: {}", request.getId());
            }
        } catch (Exception e) {
            logger.error("Push delivery error", e);
            throw new RuntimeException("Push delivery failed", e);
        }
    }

    private String formatPushNotification(Notification request) {
        return String.format("%s|%s|%s",
                request.getType(),
                request.getMessage(),
                request.getCreatedAt());
    }
}