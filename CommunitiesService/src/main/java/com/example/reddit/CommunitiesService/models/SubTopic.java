package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@Document(collection = "subtopics")
public class SubTopic {
    @Id
    private String id;

    private String name;

    @DBRef
    @JsonIgnore 
    private Topic topic;


    // Private constructor used by Builder
    private SubTopic(Builder builder) {
        this.id = builder.id; 
        this.name = builder.name;
        this.topic = builder.topic;
    }

    // Start a new builder
    public static Builder builder() {
        return new Builder();
    }

    // The Builder
    public static class Builder {
        private String id;
        private String name;
        private Topic topic;

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

        public SubTopic build() {
            return new SubTopic(this);
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}