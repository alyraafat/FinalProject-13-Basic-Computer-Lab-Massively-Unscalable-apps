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
    private UUID id; // todo: check if it should be uuid or string like the object id of mongo

    @Field(name = "topic")
    private UUID topic;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Field(name = "author_id")
    private UUID authorId;

    @Field(name = "comment_ids")
    private List<Comment > comments = new ArrayList<>();

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "up_votes")
    private Integer upVotes;

    @Field(name = "down_votes")
    private Integer downVotes;

    @Field(name = "community_id")
    private UUID communityId;

    public Thread()
    {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
       this.comments=new ArrayList<>();
    }

    // Default constructor
    private Thread( Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.upVotes = builder.upVotes;
        this.downVotes = builder.downVotes;
        this.topic=builder.topic;
        this.title=builder.title;
        this.content=builder.content;
        this.authorId=builder.authorId;
        this.communityId= builder.communityId;
        this.comments= builder.comments;

    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }



    public UUID getTopic() {
        return topic;
    }



    public String getTitle() {
        return title;
    }



    public String getContent() {
        return content;
    }



    public UUID getAuthorId() {
        return authorId;
    }


    public List<Comment> getCommentIds() {
        return comments;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }



    public Integer getUpVotes() {
        return upVotes;
    }



    public Integer getDownVotes() {
        return downVotes;
    }


    public UUID getCommunityId() {
        return communityId;
    }



// Static Builder class
public static class Builder {
    private UUID id;
    private UUID topic;
    private String title;
    private String content;
    private UUID authorId;
    private List<Comment> comments;
    private LocalDateTime createdAt;
    private Integer upVotes;
    private Integer downVotes;
    private UUID communityId;

    public Builder id(UUID id) {
        this.id = id;
        return this;
    }

    public Builder topic(UUID topic) {
        this.topic = topic;
        return this;
    }

    public Builder title(String title) {
        this.title = title;
        return this;
    }

    public Builder content(String content) {
        this.content = content;
        return this;
    }

    public Builder authorId(UUID authorId) {
        this.authorId = authorId;
        return this;
    }

    public Builder comments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Builder upVotes(Integer upVotes) {
        this.upVotes = upVotes;
        return this;
    }

    public Builder downVotes(Integer downVotes) {
        this.downVotes = downVotes;
        return this;
    }

    public Builder communityId(UUID communityId) {
        this.communityId = communityId;
        return this;
    }

    public Thread build() {
        return new Thread(this);
    }
}
}

