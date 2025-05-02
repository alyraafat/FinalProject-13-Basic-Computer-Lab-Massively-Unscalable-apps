package com.example.reddit.CommunitiesService.listeners;

import com.example.reddit.CommunitiesService.clients.NotificationClient;
import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommunityEventListener {

    private final NotificationClient notifier;

    @Autowired
    public CommunityEventListener(NotificationClient notifier) {
        this.notifier = notifier;
    }

    @EventListener
    public void onMemberAdded(CommunityMemberAddedEvent evt) {
        notifier.notifyMemberAdded(
                evt.getCommunityId(),
                evt.getUserId()
        );
    }
}