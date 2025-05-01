package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "topics")
public class Topic {
    @Id
    private String id;

    private String name;

    //—EMBEDDED list of SubTopics
    private List<SubTopic> subtopics;

    //—REFERENCING the communities under this topic
    @DBRef
    private List<Community> communities;


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
        private String id;
        private String name;
        private List<SubTopic> subtopics = new ArrayList<>();
        private List<Community> communities = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder subtopics(List<SubTopic> subtopics) {
            this.subtopics = subtopics;
            return this;
        }

        public Builder communities(List<Community> communities) {
            this.communities = communities;
            return this;
        }

        public Topic build() {
            return new Topic(this);
        }
    }

    // Getters and Setters
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

    public List<SubTopic> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<SubTopic> subtopics) {
        this.subtopics = subtopics;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }
}