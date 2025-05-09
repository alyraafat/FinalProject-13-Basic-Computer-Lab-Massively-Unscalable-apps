package com.example.reddit.CommunitiesService.rabbitmq;

import com.example.reddit.CommunitiesService.dto.CommunityNotificationRequest;
import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.models.MemberDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommunityProducer {
    private final RabbitTemplate rabbitTemplate;

    public CommunityProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyMemberAdded(UUID userID) {
        MemberDTO memberDTO = new MemberDTO(userID);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "community.memberAdded",
                memberDTO
        );

        System.out.println("User ID: " + memberDTO.getId() + " sent ");
    }

    public void notifyCommunity(Community community) {
        CommunityNotificationRequest notificationRequest = new CommunityNotificationRequest(
                community.getId(),
                community.getName(),
                community.getMemberIds()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_NOTIFICATION,
                notificationRequest
        );

        System.out.println("Notified Community with ID: " + notificationRequest.getCommunityID());
    }
}
