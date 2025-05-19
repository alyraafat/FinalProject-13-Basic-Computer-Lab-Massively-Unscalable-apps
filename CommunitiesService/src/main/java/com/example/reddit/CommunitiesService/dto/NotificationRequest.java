package com.example.reddit.CommunitiesService.dto;

import com.example.reddit.CommunitiesService.enums.NotificationType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class NotificationRequest {
    private String rawMessage;
    private List<UUID> receiversId;
    private UUID senderId;
    private NotificationType type;
    private Instant createdAt;
    private String senderName;



    public NotificationRequest(String rawMessage, List<UUID> receiversId, UUID senderId, String senderName) {
        this.rawMessage = rawMessage;
        this.receiversId = receiversId;
        this.type = NotificationType.fromString("community");
        this.senderId = senderId;
        this.senderName = senderName;
        this.createdAt = Instant.now();
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    // Getters and setters (optional, if needed)
    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public List<UUID> getReceiversId() {
        return receiversId;
    }

    public void setReceiversId(List<UUID> receiversId) {
        this.receiversId = receiversId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}