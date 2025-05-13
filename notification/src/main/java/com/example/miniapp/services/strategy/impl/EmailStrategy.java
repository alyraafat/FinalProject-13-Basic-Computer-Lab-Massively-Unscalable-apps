package com.example.miniapp.services.strategy.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class EmailStrategy implements DeliveryStrategy {

    @Override
    public void deliver(UserNotification userNotification, Notification notification) {
        try {
            System.out.println("delivering mail");
            String emailContent = formatEmail(notification);
            boolean delivered = sendEmail(emailContent);

            if (!delivered) {
                System.err.println("Email delivery failed for notification: " + notification.getId());
            }
            else{
                System.out.println("Email delivered for notification: " + notification.getId());
            }

        } catch (Exception e) {
            System.err.println("Email delivery error: " + e.getMessage());
            throw new RuntimeException("Email delivery failed", e);
        }
    }

    private String formatEmail(Notification notification) {
        return String.format(
                "Subject: Notification - %s\n" +
                        "To: user@example.com\n" +
                        "Body: %s\n" +
                        "Timestamp: %s\n" +
                        "Type: %s",
                notification.getType(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.getType()
        );
    }

    private boolean sendEmail(String emailContent) {
        try {
            System.out.println("[EMAIL] Simulation - would send:\n" + emailContent);
            return true; // Simulate successful send
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }
}