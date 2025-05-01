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
    private String id;

    private String name;

    @DBRef
    @JsonIgnore
    private Topic topic;

    private String description;

    private LocalDateTime createdAt;

    // Reference to the user who created this community
    private User createdBy;

    // References to the users in various roles, now as lists
    private List<User> moderators;

    @DBRef
    private List<User> members;

    @DBRef
    private List<User> bannedUsers;

    // Embedded list of threads
    private List<Thread> threads;


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
        private String id;
        private String name;
        private Topic topic;
        private String description;
        private LocalDateTime createdAt = LocalDateTime.now();
        private User createdBy;
        private List<User> moderators = new ArrayList<>();
        private List<User> members = new ArrayList<>();
        private List<User> bannedUsers = new ArrayList<>();
        private List<Thread> threads = new ArrayList<>();

        public Builder id(String id) {
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

        public Builder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder moderators(List<User> moderators) {
            this.moderators = moderators;
            return this;
        }

        public Builder members(List<User> members) {
            this.members = members;
            return this;
        }

        public Builder bannedUsers(List<User> bannedUsers) {
            this.bannedUsers = bannedUsers;
            return this;
        }

        public Builder threads(List<Thread> threads) {
            this.threads = threads;
            return this;
        }

        public Community build() {
            return new Community(this);
        }
    }


    // Generate getter and setter methods for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<User> getModerators() {
        return moderators;
    }

    public void setModerators(List<User> moderators) {
        this.moderators = moderators;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<User> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(List<User> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

}




    


