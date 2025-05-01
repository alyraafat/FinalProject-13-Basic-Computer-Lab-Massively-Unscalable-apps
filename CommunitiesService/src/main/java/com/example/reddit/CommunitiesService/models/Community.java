package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(collection = "communities")
public class Community {
    @Id
    private UUID id;

    @Field(name = "name")
    private String name;

    @Field(name = "topic_id")
    private UUID topicId;

    @Field(name = "description")
    private String description;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "created_by")
    private UUID createdBy;

    @Field(name = "moderator_ids")
    private Set<UUID> moderatorIds = new HashSet<>();

    @Field(name = "member_ids")
    private Set<UUID> memberIds = new HashSet<>();

    @Field(name = "banned_user_ids")
    private Set<UUID> bannedUserIds = new HashSet<>();

    @Field(name = "thread_ids")
    private List<UUID> threadIds;

    // Default constructor
    public Community() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getTopicId() {
        return topicId;
    }

    public void setTopicId(UUID topicId) {
        this.topicId = topicId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Set<UUID> getModeratorIds() {
        return moderatorIds;
    }

    public void setModeratorIds(Set<UUID> moderatorIds) {
        this.moderatorIds = moderatorIds;
    }

    public Set<UUID> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<UUID> memberIds) {
        this.memberIds = memberIds;
    }

    public Set<UUID> getBannedUserIds() {
        return bannedUserIds;
    }

    public void setBannedUserIds(Set<UUID> bannedUserIds) {
        this.bannedUserIds = bannedUserIds;
    }

    public List<UUID> getThreadIds() {
        return threadIds;
    }

    public void setThreadIds(List<UUID> threadIds) {
        this.threadIds = threadIds;
    }
}