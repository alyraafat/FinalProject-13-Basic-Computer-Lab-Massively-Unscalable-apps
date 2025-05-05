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

    private List<UUID> communityIds;

    private Topic(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.subtopicIds = builder.subtopicIds;
        this.communityIds = builder.communityIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private List<UUID> subtopicIds = new ArrayList<>();
        private List<UUID> communityIds = new ArrayList<>();

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

        public Builder communityIds(List<UUID> communities) {
            this.communityIds = communities;
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

    public List<UUID> getSubtopicIds() {
        return subtopicIds;
    }

    public void setSubtopicIds(List<UUID> subtopics) {
        this.subtopicIds = subtopics;
    }

    public List<UUID> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<UUID> communities) {
        this.communityIds = communities;
    }
}