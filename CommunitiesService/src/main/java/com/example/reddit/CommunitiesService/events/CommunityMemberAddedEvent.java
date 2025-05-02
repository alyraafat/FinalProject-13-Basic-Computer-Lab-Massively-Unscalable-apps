package com.example.reddit.CommunitiesService.events;

import java.util.UUID;

public class CommunityMemberAddedEvent {
    private final UUID communityId;
    private final UUID userId;

    public CommunityMemberAddedEvent(UUID communityId, UUID userId) {
        this.communityId = communityId;
        this.userId = userId;
    }

    public UUID getCommunityId() {
        return communityId;
    }

    public UUID getUserId() {
        return userId;
    }
}