package com.example.reddit.ThreadsService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "threads")
public class Thread {
    @Id
    private UUID id;

    @Field(name = "topic")
    private String topic;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Field(name = "author_id")
    private UUID authorId;

    @Field(name = "comment_ids")
    private List<UUID> commentIds = new ArrayList<>();

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "up_votes")
    private Integer upVotes;

    @Field(name = "down_votes")
    private Integer downVotes;

    @Field(name = "community_id")
    private UUID communityId;

    // Default constructor
    public Thread(String topic , String title , String content , UUID authorId, UUID communityId) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.upVotes = 0;
        this.downVotes = 0;
        this.topic=topic;
        this.title=title;
        this.content=content;
        this.authorId=authorId;
        this.communityId=communityId;
        this.commentIds= new ArrayList<>();

    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public List<UUID> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<UUID> commentIds) {
        this.commentIds = commentIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Integer upVotes) {
        this.upVotes = upVotes;
    }

    public Integer getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Integer downVotes) {
        this.downVotes = downVotes;
    }

    public UUID getCommunityId() {
        return communityId;
    }

    public void setCommunityId(UUID communityId) {
        this.communityId = communityId;
    }
}