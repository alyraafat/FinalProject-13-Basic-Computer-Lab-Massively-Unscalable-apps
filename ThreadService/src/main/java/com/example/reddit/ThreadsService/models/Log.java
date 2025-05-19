package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "logs")
public abstract class Log {
    @Id
    private UUID id;

    @Field(name = "user_id")
    private UUID userId;

    @Field(name = "action_type")
    private ActionType actionType;

    @Field(name = "thread_id")
    private UUID threadId;

    @Field(name = "timestamp")
    private LocalDateTime timestamp;

    private Integer voteCount=0 ;

    // Default constructor
    public Log(UUID userId, ActionType actionType, UUID threadId,Integer voteCount) {

        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.userId = userId;
        this.actionType = actionType;
        this.threadId = threadId;
        this.voteCount=voteCount;
    }

    public abstract String getLogType();

    public abstract Log createLog(UUID userId, UUID threadId);

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public void setThreadId(UUID threadId) {
        this.threadId = threadId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}