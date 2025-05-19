package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;


import java.util.UUID;

public class Comment {
    @Id
    private UUID id;

    private String content;

    private UUID threadId;

    private UUID userId;

    // Default constructor
    public Comment(String content , UUID threadId, UUID userId) {
        this.id = UUID.randomUUID();
        this.content=content;
        this.threadId=threadId;
        this.userId=userId;

    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public void setThreadId(UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}