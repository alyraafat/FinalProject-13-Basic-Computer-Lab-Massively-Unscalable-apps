package com.example.miniapp.models;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notification_requests")
public class NotificationRequest {

    // PRIMARY KEY
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // CORE CONTENT
    @Column(nullable = false, length = 1000)
    private String rawMessage;

    @Column(nullable = false)
    private UUID receiverId;

    // SYSTEM METADATA
    @Column(nullable = false, name = "origin_system")
    private String originSystem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // DELIVERY CONTROL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgency;

    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    // HUMAN CONTEXT
    @Column(name = "actor_user_id")
    private String actorUserId;

    // JPA REQUIREMENTS
    protected NotificationRequest() {}

    public NotificationRequest(String rawMessage,
                               UUID receiverId,
                               String originSystem,
                               NotificationType type,
                               UrgencyLevel urgency,
                               String actorUserId) {
        this.rawMessage = validateMessage(rawMessage);
        this.receiverId = Objects.requireNonNull(receiverId);
        this.originSystem = validateSystem(originSystem);
        this.type = Objects.requireNonNull(type);
        this.urgency = Objects.requireNonNull(urgency);
        this.createdAt = Instant.now();
        this.actorUserId = actorUserId;
    }

    // VALIDATION HELPERS
    private String validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be blank");
        }
        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }

    private String validateSystem(String system) {
        if (system == null || system.isBlank()) {
            throw new IllegalArgumentException("Origin system cannot be blank");
        }
        return system.trim();
    }

    // GETTERS
    public UUID getId() { return id; }
    public String getRawMessage() { return rawMessage; }
    public UUID getReceiverId() { return receiverId; }
    public String getOriginSystem() { return originSystem; }
    public NotificationType getType() { return type; }
    public UrgencyLevel getUrgency() { return urgency; }
    public Instant getCreatedAt() { return createdAt; }
    public String getActorUserId() { return actorUserId; }
}