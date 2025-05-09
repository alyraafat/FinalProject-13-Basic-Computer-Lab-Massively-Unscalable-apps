package com.example.reddit.ThreadsService.models;



import com.example.reddit.ThreadsService.models.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Component
public class ThreadDto {

    private UUID id;
    private UUID topic;
    private String title;
    private String content;
    private UUID authorId;
    private List<Comment> commentIds;
    private LocalDateTime createdAt;
    private Integer upVotes;
    private Integer downVotes;
    private UUID communityId;

    public ThreadDto() {
        // Default constructor for deserialization (e.g., by Jackson)
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTopic() {
        return topic;
    }

    public void setTopic(UUID topic) {
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

    public List<Comment> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<Comment> comments) {
        this.commentIds = comments;
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
