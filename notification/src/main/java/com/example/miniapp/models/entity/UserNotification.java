package com.example.miniapp.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.JoinColumn;


@Entity
@Table(name = "user_notifications")
public class UserNotification {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private UUID userId;
    private String status;
    private Instant readAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public UserNotification(UUID userId, Notification notification) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.notification = notification;
        this.status = "unread";
        this.readAt = null;
    }

    public UserNotification() {
        // Default constructor
    }
}