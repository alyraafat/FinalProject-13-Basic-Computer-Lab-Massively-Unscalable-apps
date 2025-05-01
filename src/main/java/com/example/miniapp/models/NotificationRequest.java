package com.example.miniapp.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        String userId,
        NotificationType type,
        String sourceId,  // postId/commentId/communityId
        String triggerUserId, // who caused the notification
        String customMessage,
        Metadata metadata
) {
    public NotificationType getType() {
        return type;
    }

    @Override
    public String userId() {
        return userId;
    }

    @Override
    public NotificationType type() {
        return type;
    }

    @Override
    public String sourceId() {
        return sourceId;
    }

    @Override
    public String triggerUserId() {
        return triggerUserId;
    }

    @Override
    public String customMessage() {
        return customMessage;
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    public String getUserId() {
        return userId;
    }

    public record Metadata(
            String ipAddress,
            String deviceInfo,
            String location
    ) {}
}