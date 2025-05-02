package com.example.moderator.model;


import jakarta.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "community_id", nullable = false)
    private Long communityId;

    // any extra fields (e.g. role, assignedAt) go here...

    public Moderator() {}

    public Moderator(Long userId, Long communityId) {
        this.userId = userId;
        this.communityId = communityId;
    }

    // --- getters & setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommunityId() {
        return communityId;
    }
    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}

