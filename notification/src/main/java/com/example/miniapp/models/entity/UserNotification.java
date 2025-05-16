package com.example.miniapp.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "user_notifications")
public class UserNotification {

    @Id
    private UUID id;

    private UUID notificationId;

    private UUID userId;
    private String status;
    private Instant readAt;

    public UserNotification() {
        // Default constructor
    }

    public UserNotification(UUID userId, Notification notification) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.notificationId = notification.getId(); // use the ID only
        this.status = "unread";
        this.readAt = null;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }
}