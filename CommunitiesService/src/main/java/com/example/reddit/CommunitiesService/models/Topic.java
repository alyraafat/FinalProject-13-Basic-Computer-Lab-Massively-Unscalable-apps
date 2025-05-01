package com.example.reddit.CommunitiesService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "topics")
public class Topic {
    @Id
    private UUID id;

    @Field(name = "name")
    private String name;

    @Field(name = "community_ids")
    private List<UUID> communityIds = new ArrayList<>();

    @Field(name = "subtopic_ids")
    private List<UUID> subtopicIds = new ArrayList<>();

    // Default constructor
    public Topic() {
        this.id = UUID.randomUUID();
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

    public List<UUID> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<UUID> communityIds) {
        this.communityIds = communityIds;
    }

    public List<UUID> getSubtopicIds() {
        return subtopicIds;
    }

    public void setSubtopicIds(List<UUID> subtopicIds) {
        this.subtopicIds = subtopicIds;
    }
}