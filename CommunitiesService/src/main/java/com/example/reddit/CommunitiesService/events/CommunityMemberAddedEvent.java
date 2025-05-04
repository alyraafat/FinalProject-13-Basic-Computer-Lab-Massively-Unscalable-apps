package com.example.reddit.CommunitiesService.events;

import java.util.UUID;

public class CommunityMemberAddedEvent {
    private final String type;
    private final UUID communityId;
    private final String message;

    public CommunityMemberAddedEvent(String type, UUID communityId, String message) {
        this.type = type;
        this.communityId = communityId;
        this.message = message;
    }

    public UUID getCommunityId() {
        return communityId;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}