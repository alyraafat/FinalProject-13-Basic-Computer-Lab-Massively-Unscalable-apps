package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "subtopics")
public class SubTopic {
    @Id
    private UUID id;

    private String name;

    private UUID topicId;

    // Private constructor used by Builder
    private SubTopic(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.topicId = builder.topicId;
    }

    // Start a new builder
    public static Builder builder() {
        return new Builder();
    }

    // The Builder
    public static class Builder {
        private UUID id;
        private String name;
        private UUID topicId;

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

        public SubTopic build() {
            return new SubTopic(this);
        }
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

    public void setTopicId(UUID topic) {
        this.topicId = topic;
    }
}