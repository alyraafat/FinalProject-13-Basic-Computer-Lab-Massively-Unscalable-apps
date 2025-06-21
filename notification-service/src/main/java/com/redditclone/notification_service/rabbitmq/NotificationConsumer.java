package com.redditclone.notification_service.rabbitmq;

import com.redditclone.notification_service.models.dto.NotificationRequest;
import com.redditclone.notification_service.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.MULTI_QUEUE)
    public void onNotificationRequest(NotificationRequest request) {
        // simple console output instead of a logger
        System.out.println("Received notification request: " + request.getRawMessage());
        notificationService.process(request);

    }
}
