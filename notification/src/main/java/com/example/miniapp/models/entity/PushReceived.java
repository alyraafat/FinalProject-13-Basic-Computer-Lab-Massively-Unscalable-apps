package com.example.miniapp.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "push_receives")
public class PushReceived {

    @Id
    private UUID id;
    private String title;
    private String message;
    private Instant createdAt;
    private UUID receiverId;


    public PushReceived(String title, String message, Instant createdAt, UUID receiverId) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.receiverId = receiverId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }



}
