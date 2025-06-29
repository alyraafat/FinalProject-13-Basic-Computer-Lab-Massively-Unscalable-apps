package com.redditclone.notification_service.models.dto;

import com.redditclone.notification_service.models.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
public class NotificationRequest {
    // Getters and setters (optional, if needed)
    private String rawMessage;
    private List<UUID> receiversId;
    private UUID senderId;
    private NotificationType type;
    private Instant createdAt;
    private String senderName;

    public NotificationRequest() {
        this.createdAt = Instant.now();
    }

    public NotificationRequest(String rawMessage, List<UUID> receiversId, UUID senderId, String senderName, NotificationType type) {
        this.rawMessage = rawMessage;
        this.receiversId = receiversId;
        this.type = type;
        this.senderId = senderId;
        this.senderName = senderName;
        this.createdAt = Instant.now();
    }

}
