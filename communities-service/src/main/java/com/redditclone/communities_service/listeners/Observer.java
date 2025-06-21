package com.redditclone.communities_service.listeners;

import com.redditclone.communities_service.events.CommunityMemberAddedEvent;

public interface Observer {

    void update(CommunityMemberAddedEvent evt);

}
