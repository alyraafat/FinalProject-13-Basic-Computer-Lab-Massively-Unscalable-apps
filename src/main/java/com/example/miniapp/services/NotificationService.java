package com.example.miniapp.services;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // For push notifications
    public boolean sendPush(String formattedPayload) {
        try {
            logger.info("Sending push notification: {}", formattedPayload);
            System.out.println("[PUSH] " + formattedPayload);
            return true; // Assume success for demo
        } catch (Exception e) {
            logger.error("Failed to send push notification", e);
            return false;
        }
    }

    // For email notifications
    public boolean sendEmail(String emailContent) {
        try {
            logger.info("Sending email notification");
            System.out.println("[EMAIL]\n" + emailContent);
            return true; // Assume success for demo
        } catch (Exception e) {
            logger.error("Failed to send email notification", e);
            return false;
        }
    }

}