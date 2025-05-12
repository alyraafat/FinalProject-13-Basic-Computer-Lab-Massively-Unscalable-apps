package com.example.reddit.CommunitiesService.events;

import java.util.UUID;

public class CommunityMemberAddedEvent {
    private final UUID userId;


    public CommunityMemberAddedEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}