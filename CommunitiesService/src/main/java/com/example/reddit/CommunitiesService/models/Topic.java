package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "topics")
public class Topic {
    @Id
    private UUID id;

    private String name;

    private List<UUID> subtopicIds;

    // Default constructor
    public Topic() {
        this.id = UUID.randomUUID();
        this.subtopicIds = new ArrayList<>();
    }

    private Topic(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.subtopicIds = builder.subtopicIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private List<UUID> subtopicIds = new ArrayList<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder subtopicIds(List<UUID> subtopics) {
            this.subtopicIds = subtopics;
            return this;
        }

        public Topic build() {
            return new Topic(this);
        }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getSubtopicIds() {
        return subtopicIds;
    }

}