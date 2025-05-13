package com.example.miniapp.services.strategy.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.PushReceived;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.repositories.PushReceivedRepository;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushStrategy implements DeliveryStrategy {
    @Autowired
    private PushReceivedRepository pushReceivedRepository;


    @Override
    public void deliver(UserNotification userNotification, Notification notification) {
        try {
            System.out.println("Delivering notification: " + notification.getId());
            boolean delivered = sendPush(userNotification, notification);
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

    private boolean sendPush(UserNotification userNotification, Notification notification) {
        try {
            String formattedPayload = formatPushNotification(notification);
            System.out.println("[PUSH] Simulation - would send: " + formattedPayload);
            PushReceived pushReceived = new PushReceived(
                    notification.getTitle(),
                    formattedPayload + "from" + notification.getSenderName(),
                    notification.getCreatedAt(),
                    userNotification.getUserId()
            );
            pushReceivedRepository.save(pushReceived);
            return true; // Simulate successful send
        } catch (Exception e) {
            System.err.println("Failed to send push: " + e.getMessage());
            throw e;
        }
    }
}