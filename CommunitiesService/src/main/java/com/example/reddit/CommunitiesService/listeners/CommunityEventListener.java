package com.example.reddit.CommunitiesService.listeners;

import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;
import com.example.reddit.CommunitiesService.rabbitmq.CommunityProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommunityEventListener {

    private final CommunityProducer producer;

    @Autowired
    public CommunityEventListener(CommunityProducer producer) {
        this.producer = producer;
    }

    @EventListener
    public void onMemberAdded(CommunityMemberAddedEvent evt) {
        producer.notifyMemberAdded(
                evt.getUserId()
        );
    }
}