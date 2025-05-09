package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "communities")
public class Community {
    @Id
    private UUID id;

    private String name;

    private UUID topicId;

    private String description;

    private LocalDateTime createdAt;

    // Reference to the user who created this community
    private UUID createdBy;

    private List<UUID> moderatorIds;

    private List<UUID> memberIds;

    private List<UUID> bannedUserIds;

    private List<UUID> threadIds;

    // Default constructor
    public Community() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.moderatorIds = new ArrayList<>();
        this.memberIds = new ArrayList<>();
        this.bannedUserIds = new ArrayList<>();
        this.threadIds = new ArrayList<>();
    }

    private Community(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.topicId = builder.topicId;
        this.description = builder.description;
        this.createdAt = builder.createdAt;
        this.createdBy = builder.createdBy;
        this.moderatorIds = builder.moderatorIds;
        this.memberIds = builder.memberIds;
        this.bannedUserIds = builder.bannedUserIds;
        this.threadIds = builder.threadIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private UUID topicId;
        private String description;
        private LocalDateTime createdAt = LocalDateTime.now();
        private UUID createdBy;
        private List<UUID> moderatorIds = new ArrayList<>();
        private List<UUID> memberIds = new ArrayList<>();
        private List<UUID> bannedUserIds = new ArrayList<>();
        private List<UUID> threadIds = new ArrayList<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder topicId(UUID topic) {
            this.topicId = topic;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdBy(UUID createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder moderatorIds(List<UUID> moderators) {
            this.moderatorIds = moderators;
            return this;
        }

        public Builder memberIds(List<UUID> members) {
            this.memberIds = members;
            return this;
        }

        public Builder bannedUserIds(List<UUID> bannedUsers) {
            this.bannedUserIds = bannedUsers;
            return this;
        }

        public Builder threadIds(List<UUID> threads) {
            this.threadIds = threads;
            return this;
        }

        public Community build() {
            return new Community(this);
        }
    }

    // Generate getter and setter methods for all fields
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

    public void setTopicId(UUID topic) {
        this.topicId = topic;
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

    public List<UUID> getModeratorIds() {
        return moderatorIds;
    }

    public void setModeratorIds(List<UUID> moderators) {
        this.moderatorIds = moderators;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<UUID> members) {
        this.memberIds = members;
    }

    public List<UUID> getBannedUserIds() {
        return bannedUserIds;
    }

    public void setBannedUserIds(List<UUID> bannedUsers) {
        this.bannedUserIds = bannedUsers;
    }

    public List<UUID> getThreadIds() {
        return threadIds;
    }

    public void setThreadIds(List<UUID> threads) {
        this.threadIds = threads;
    }

}
