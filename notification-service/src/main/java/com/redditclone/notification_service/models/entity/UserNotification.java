package com.redditclone.notification_service.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Document(collection = "user_notifications")
public class UserNotification {

    @Id
    private String id;

    @DocumentReference
    private Notification notification;

    private String deliveredMessage;
    private UUID userId;
    private String status;
    private Instant readAt;

    public UserNotification() {
        // Default constructor
    }

    public UserNotification(Notification notification, UUID userId, String status, String deliveredMessage) {
        this.notification = notification;
        this.userId = userId;
        this.status = status;
        this.readAt = null; // Default value for readAt
        this.deliveredMessage = deliveredMessage;
    }

}