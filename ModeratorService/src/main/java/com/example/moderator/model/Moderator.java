package com.example.moderator.model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "moderators",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_moderator_user_community",
                columnNames = {"user_id", "community_id"}
        )
)
public class Moderator {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "community_id", nullable = false)
    private UUID communityId;

    // any extra fields (e.g. role, assignedAt) go here...

    public Moderator() {}

    public Moderator(UUID userId, UUID communityId) {
        this.userId = userId;
        this.communityId = communityId;
    }

    // --- getters & setters ---
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCommunityId() {
        return communityId;
    }
    public void setCommunityId(UUID communityId) {
        this.communityId = communityId;
    }
}

