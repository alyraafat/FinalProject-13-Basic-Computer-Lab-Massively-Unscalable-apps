package com.example.reddit.CommunitiesService.events;

import com.example.reddit.CommunitiesService.models.Community;

import java.util.List;
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