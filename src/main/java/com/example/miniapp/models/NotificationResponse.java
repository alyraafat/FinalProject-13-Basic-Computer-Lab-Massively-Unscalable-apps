package com.example.miniapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationResponse(
        Long id,
        String userId,
        NotificationType type,
        String content,
        boolean read,
        LocalDateTime createdAt,
        String sourceId,
        String triggerUserAvatar,
        String triggerUsername,
        String deepLink
) {
    public static NotificationResponse fromEntity(Notification entity) {
        return new NotificationResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getContent(),
                entity.isRead(),
                entity.getCreatedAt(),
                entity.getSourceId(),
                null, // Would be populated from user service
                null, // Would be populated from user service
                generateDeepLink(entity)
        );
    }

    private static String generateDeepLink(Notification entity) {
        return switch (entity.getType().getCategory()) {
            case "thread" -> "/thread/" + entity.getSourceId();
            case "community" -> "/c/" + entity.getSourceId();
            default -> null;
        };
    }
}