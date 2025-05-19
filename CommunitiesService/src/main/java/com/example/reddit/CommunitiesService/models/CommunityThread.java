package com.example.reddit.CommunitiesService.models;


import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommunityThread {
    @Id
    private UUID id;

    private UUID topic;

    private String title;

    private String content;

    private UUID authorId;

    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;

    private Integer upVotes;

    private Integer downVotes;

    private UUID communityId;


    public CommunityThread() { }

    // Default constructor
    private CommunityThread( Builder builder) {
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

        public CommunityThread build() {
            return new CommunityThread(this);
        }
    }
}