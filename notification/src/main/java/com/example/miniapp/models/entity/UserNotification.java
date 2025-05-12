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

    @DBRef
    private Notification notification;

    private UUID userId;
    private String status;
    private Instant readAt;

    public UserNotification() {
        // Default constructor
    }

    public UserNotification(UUID userId, Notification notification) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.notification = notification;
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

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
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