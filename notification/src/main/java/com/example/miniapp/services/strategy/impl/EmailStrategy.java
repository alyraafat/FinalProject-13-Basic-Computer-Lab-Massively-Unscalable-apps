package com.example.miniapp.services.strategy.impl;

import com.example.miniapp.models.dto.NotificationEmail;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.services.MailService;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailStrategy implements DeliveryStrategy {

    private MailService mailService;

    @Override
    public void deliver(Notification notification) {
        NotificationEmail emailContent = new NotificationEmail(
                "Reddit Clone" + notification.getType(),
                "Reddit Clone" + notification.getTitle(),
                notification.getReceiversId().getFirst().toString(),
                notification.getMessage()
        );
        mailService.sendMail(emailContent);
    }

//    private String formatEmail(Notification notification) {
//        return String.format(
//                "Subject: Notification - %s\n" +
//                        "To: user@example.com\n" +
//                        "Body: %s\n" +
//                        "Timestamp: %s\n" +
//                        "Type: %s",
//                notification.getType(),
//                notification.getMessage(),
//                notification.getCreatedAt(),
//                notification.getType()
//        );
//    }

//    private boolean sendEmail(String emailContent) {
//        try {
//            System.out.println("[EMAIL] Simulation - would send:\n" + emailContent);
//            return true; // Simulate successful send
//        } catch (Exception e) {
//            System.err.println("Failed to send email: " + e.getMessage());
//            return false;
//        }
//    }
}