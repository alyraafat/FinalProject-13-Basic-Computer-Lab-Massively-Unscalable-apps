package com.redditclone.communities_service.events;

import com.redditclone.communities_service.models.Community;

import java.util.UUID;

public class CommunityMemberAddedEvent {
    private final UUID userId;
    private final Community community;


    public CommunityMemberAddedEvent(UUID userId, Community community) {
        this.userId = userId;
        this.community = community;
    }

    public UUID getUserId() {
        return userId;
    }

    public Community getCommunity(){
        return community;
    }
}