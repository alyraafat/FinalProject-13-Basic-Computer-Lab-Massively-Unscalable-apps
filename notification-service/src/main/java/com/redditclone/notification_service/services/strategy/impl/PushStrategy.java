package com.redditclone.notification_service.services.strategy.impl;

import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.entity.PushReceived;
import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.repositories.PushReceivedRepository;
import com.redditclone.notification_service.services.strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component("pushStrategy")
public class PushStrategy implements DeliveryStrategy {
    private final PushReceivedRepository pushReceivedRepository;

    public PushStrategy(PushReceivedRepository pushReceivedRepository) {
        this.pushReceivedRepository = pushReceivedRepository;
    }


    @Override
    public void deliver(UserNotification userNotification) {
        try {
            Notification notification = userNotification.getNotification();
            System.out.println("Delivering notification: " + notification.getId());
            boolean delivered = sendPush(userNotification, notification);
            if (!delivered) {
                System.err.println("Push delivery failed for notification: " + notification.getId());
            } else {
                System.out.println("Notification delivered for notification: " + notification.getId());
            }
        } catch (Exception e) {
            System.err.println("Push delivery error: " + e.getMessage());
            throw new RuntimeException("Push delivery failed", e);
        }
    }

    private String formatPushNotification(UserNotification userNotification) {
        return String.format("%s|%s|%s",
                userNotification.getNotification().getType(),
                userNotification.getDeliveredMessage(),
                userNotification.getNotification().getCreatedAt());
    }

    private boolean sendPush(UserNotification userNotification, Notification notification) {
        try {
            String formattedPayload = formatPushNotification(userNotification);
            System.out.println("[PUSH] Simulation - would send: " + formattedPayload);
            PushReceived pushReceived = new PushReceived(
                    notification.getTitle(),
                    formattedPayload + "from" + notification.getSenderName(),
                    notification.getCreatedAt(),
                    userNotification.getUserId()
            );
            pushReceivedRepository.save(pushReceived);
            System.out.println("Push saved to repository: " + pushReceived);
            return true; // Simulate successful send
        } catch (Exception e) {
            System.err.println("Failed to send push: " + e.getMessage());
            throw e;
        }
    }
}