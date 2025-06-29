package com.redditclone.communities_service.rabbitmq;

import com.redditclone.communities_service.dto.NotificationRequest;
import com.redditclone.communities_service.enums.NotificationType;
import com.redditclone.communities_service.models.Community;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;


@Service
public class CommunityProducer {
    private final RabbitTemplate rabbitTemplate;

    public CommunityProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyNewMember(UUID uuid, Community community) {
        String message = "A new member has joined the community. Greet him!!!";
        List<UUID> recieversIDs = community.getMemberIds();
        NotificationRequest notificationRequest = new NotificationRequest(message, recieversIDs, uuid, community.getName());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_NOTIFICATION,
                notificationRequest
        );

        List<UUID> newMembersIDs = new ArrayList<>();
        newMembersIDs.add(uuid);
        message = "Welcome to your new home!! " + community.getName() + " is happy with you!";
        NotificationRequest notificationRequest2 = new NotificationRequest(message, newMembersIDs, community.getId(), community.getName());
        notificationRequest2.setType(NotificationType.USER_SPECIFIC);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_NOTIFICATION,
                notificationRequest2
        );
        System.out.println("Notifications has been sent to the community members");
    }

    public void notifyNewThread(Community community) {
        String rawMessage = "A new thread is in " + community.getName() + " community. View it now !!!";
        UUID senderID = community.getId();
        List<UUID> recieversID = community.getMemberIds();
        String name = community.getName();
        NotificationRequest notificationRequest = new NotificationRequest(rawMessage, recieversID, senderID, name);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_NOTIFICATION,
                notificationRequest
        );
        System.out.println("Notified Community with ID: " + notificationRequest.getSenderName());
    }
}
