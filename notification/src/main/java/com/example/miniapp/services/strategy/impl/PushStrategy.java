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

    @Override
    public void deliver(Notification notification) {
        try {
            System.out.println("Delivering notification: " + notification.getId());
            String pushPayload = formatPushNotification(notification);
            boolean delivered = sendPush(pushPayload);

            if (!delivered) {
                System.err.println("Push delivery failed for notification: " + notification.getId());
            }else{
                System.out.println("Notification delivered for notification: " + notification.getId());
            }
        } catch (Exception e) {
            System.err.println("Push delivery error: " + e.getMessage());
            throw new RuntimeException("Push delivery failed", e);
        }
    }

    private String formatPushNotification(Notification notification) {
        return String.format("%s|%s|%s",
                notification.getType(),
                notification.getMessage(),
                notification.getCreatedAt());
    }

    private boolean sendPush(String formattedPayload) {
        try {
            System.out.println("[PUSH] Simulation - would send: " + formattedPayload);
            return true; // Simulate successful send
        } catch (Exception e) {
            System.err.println("Failed to send push: " + e.getMessage());
            return false;
        }
    }
}