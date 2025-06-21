package com.redditclone.communities_service.enums;

public enum NotificationType {
    THREAD, COMMUNITY, USER_SPECIFIC;

    public static NotificationType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }

        return switch (type.toLowerCase()) {
            case "user" -> USER_SPECIFIC;
            case "community" -> COMMUNITY;
            case "thread" -> THREAD;
            default -> throw new IllegalArgumentException("Unknown notification type: " + type);
        };
    }
}