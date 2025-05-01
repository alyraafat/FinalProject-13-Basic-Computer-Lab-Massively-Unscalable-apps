package com.example.miniapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NotificationType {
    SECURITY_LOGIN("security", "login", "New login detected"),
    SECURITY_PASSWORD_CHANGE("security", "password_change", "Password updated"),
    COMMUNITY_ANNOUNCEMENT("community", "announcement", "New announcement"),
    THREAD_REPLY("thread", "reply", "New reply to your thread"),
    COMMENT_MENTION("thread", "mention", "You were mentioned");

    private final String category;
    private final String code;
    private final String defaultMessage;

    NotificationType(String category, String code, String defaultMessage) {
        this.category = category;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    // Getters
    public String getCategory() { return category; }
    public String getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }
    public String getName() { return this.name(); }

    public static NotificationType fromCode(String code) {
        for (NotificationType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown notification code: " + code);
    }
}