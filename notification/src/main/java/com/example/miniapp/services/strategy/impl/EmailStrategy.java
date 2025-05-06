package com.example.miniapp.services.strategy.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.services.NotificationService;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EmailStrategy implements DeliveryStrategy {
    private static final Logger logger = LoggerFactory.getLogger(EmailStrategy.class);

    private final NotificationService notificationService;

    public EmailStrategy(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void deliver(NotificationRequest request) {
        try {
            String emailContent = formatEmail(request);

            boolean delivered = notificationService.sendEmail(emailContent);

            if (!delivered) {
                logger.warn("Email delivery failed for request: {}", request.getId());
            }
        } catch (Exception e) {
            logger.error("Email delivery error", e);
            throw new RuntimeException("Email delivery failed", e);
        }
    }

    private String formatEmail(NotificationRequest request) {
        return String.format(
                "Subject: Notification - %s\n" +
                        "To: user@example.com\n" +
                        "Body: %s\n" +
                        "Timestamp: %s\n" +
                        "Type: %s",
                request.getType(),
                request.getRawMessage(),
                request.getCreatedAt(),
                request.getType()
        );
    }
}