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

    private List<UUID> subtopics;

    private List<UUID> communities;


    private Topic(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.subtopics = builder.subtopics;
        this.communities = builder.communities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private List<UUID> subtopics = new ArrayList<>();
        private List<UUID> communities = new ArrayList<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder subtopics(List<UUID> subtopics) {
            this.subtopics = subtopics;
            return this;
        }

        public Builder communities(List<UUID> communities) {
            this.communities = communities;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<UUID> subtopics) {
        this.subtopics = subtopics;
    }

    public List<UUID> getCommunities() {
        return communities;
    }

    public void setCommunities(List<UUID> communities) {
        this.communities = communities;
    }
}