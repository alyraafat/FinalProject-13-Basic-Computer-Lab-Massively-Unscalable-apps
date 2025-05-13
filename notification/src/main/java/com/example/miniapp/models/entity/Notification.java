package com.example.miniapp.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "notifications")
public class Notification {

    @Id
    private UUID id;

    private String type;
    private String senderId;
    private String title;
    private String message;
    private Instant createdAt;
    private String senderName;
    @Field("receivers_id")
    private List<UUID> receiversId;

    // Getters and Setters

    public Notification()
    {
        this.id=UUID.randomUUID();
        this.receiversId= new ArrayList<>();

    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<UUID> getReceiversId() {
        return receiversId;
    }

    public void setReceiversId(List<UUID> receiversId) {
        this.receiversId = receiversId;
    }
}