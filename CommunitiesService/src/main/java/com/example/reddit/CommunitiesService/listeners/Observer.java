package com.example.reddit.CommunitiesService.listeners;

import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;

public interface Observer {

    void update(CommunityMemberAddedEvent evt);

}
