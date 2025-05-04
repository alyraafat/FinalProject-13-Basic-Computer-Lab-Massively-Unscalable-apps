package com.example.reddit.ThreadsService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "comments")
public class Comment {
    @Id
    private UUID id;

    @Field(name = "content")
    private String content;

    @Field(name = "thread_id")
    private UUID threadId;

    @Field (name="user_id")
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