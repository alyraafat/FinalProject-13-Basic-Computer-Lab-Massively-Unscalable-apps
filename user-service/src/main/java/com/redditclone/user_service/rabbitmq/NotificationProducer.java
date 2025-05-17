package com.redditclone.user_service.rabbitmq;

import com.redditclone.user_service.dtos.NotificationRequest;
import com.redditclone.user_service.models.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;


@Service
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyUser(User user) {
        String rawMessage = "Welcome! \n Thank you for trusting us and joining Reddit Clone!";
        UUID senderID = UUID.randomUUID();
        UUID receiverID = user.getId();
        String name = user.getFullName();
        List<UUID> recievers = new ArrayList<>();
        recievers.add(receiverID);
        NotificationRequest notificationRequest = new NotificationRequest(rawMessage, recievers, senderID, name);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_NOTIFICATION,
                notificationRequest
        );

        System.out.println("Notified Community with ID: " + notificationRequest.getSenderName());
    }
}
