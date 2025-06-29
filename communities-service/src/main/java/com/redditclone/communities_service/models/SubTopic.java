package com.redditclone.communities_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "subtopics")
public class SubTopic {
    @Id
    private UUID id;

    private String name;

    // Default constructor
    public SubTopic() {
        this.id = UUID.randomUUID();
    }

    // Private constructor used by Builder
    private SubTopic(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    // Start a new builder
    public static Builder builder() {
        return new Builder();
    }

    // The Builder
    public static class Builder {
        private UUID id;
        private String name;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
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

    public String getName() {
        return name;
    }

}