package com.redditclone.communities_service.listeners;

import com.redditclone.communities_service.events.CommunityMemberAddedEvent;
import com.redditclone.communities_service.publishers.Subject;
import com.redditclone.communities_service.rabbitmq.CommunityProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener implements Observer {

    private final CommunityProducer producer;
    private final Subject communityPublisher;


    @Autowired
    public NotificationListener(CommunityProducer producer, Subject communityPublisher) {
        this.producer = producer;
        this.communityPublisher = communityPublisher;
        this.communityPublisher.registerObserver(this);
    }

    @Override
    public void update(CommunityMemberAddedEvent evt) {
        System.out.println("Observer notified of member added: " + evt.getUserId());
        producer.notifyNewMember(
                evt.getUserId(),
                evt.getCommunity()
        );
    }
}