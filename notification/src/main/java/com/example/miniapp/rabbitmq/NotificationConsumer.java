package com.example.miniapp.rabbitmq;

import com.example.miniapp.models.dto.NotificationRequest;
//import com.example.miniapp.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
//    private final NotificationService notificationService;
//
//    public NotificationConsumer(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }

    @RabbitListener(queues = RabbitMQConfig.MULTI_QUEUE)
    public void onNotificationRequest(NotificationRequest request) {
        // simple console output instead of a logger
        System.out.println("Received notification request: " + request.getSenderName());
        // TODO: Handle the request here.
    }
}
