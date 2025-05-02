package com.example.reddit.CommunitiesService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "communities")
public class Community {
    @Id
    private UUID id;

    private String name;

    @DBRef
    @JsonIgnore
    private Topic topic;

    private String description;

    private LocalDateTime createdAt;

    // Reference to the user who created this community
    private UUID createdBy;

    // References to the users in various roles, now as lists
    private List<UUID> moderators;

    @DBRef
    private List<UUID> members;

    @DBRef
    private List<UUID> bannedUsers;

    // Embedded list of threads
    private List<UUID> threads;


    private Community(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.topic = builder.topic;
        this.description = builder.description;
        this.createdAt = builder.createdAt;
        this.createdBy = builder.createdBy;
        this.moderators = builder.moderators;
        this.members = builder.members;
        this.bannedUsers = builder.bannedUsers;
        this.threads = builder.threads;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private Topic topic;
        private String description;
        private LocalDateTime createdAt = LocalDateTime.now();
        private UUID createdBy;
        private List<UUID> moderators = new ArrayList<>();
        private List<UUID> members = new ArrayList<>();
        private List<UUID> bannedUsers = new ArrayList<>();
        private List<UUID> threads = new ArrayList<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder topic(Topic topic) {
            this.topic = topic;
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

        public Builder moderators(List<UUID> moderators) {
            this.moderators = moderators;
            return this;
        }

        public Builder members(List<UUID> members) {
            this.members = members;
            return this;
        }

        public Builder bannedUsers(List<UUID> bannedUsers) {
            this.bannedUsers = bannedUsers;
            return this;
        }

        public Builder threads(List<UUID> threads) {
            this.threads = threads;
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public List<UUID> getModerators() {
        return moderators;
    }

    public void setModerators(List<UUID> moderators) {
        this.moderators = moderators;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public List<UUID> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(List<UUID> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    public List<UUID> getThreads() {
        return threads;
    }

    public void setThreads(List<UUID> threads) {
        this.threads = threads;
    }

}
