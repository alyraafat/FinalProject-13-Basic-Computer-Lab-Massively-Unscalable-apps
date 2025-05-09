package com.example.miniapp.rabbitmq;



import com.example.miniapp.models.MemberDTO;
import com.example.miniapp.models.dto.CommunityNotificationRequest;
import com.example.miniapp.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.MULTI_QUEUE)
public class NotificationConsumer {
    private final NotificationService notificationService;
    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitHandler
    public void onMemberAdded(MemberDTO memberDTO) {

        System.out.println("Member added: " + memberDTO.getId());
        // TODO: Add code to handle notification when new member is added
    }

    @RabbitHandler
    public void onCommunityNotificationRequest(CommunityNotificationRequest notificationRequest) {
        System.out.println("Received notification request from community with ID: " + notificationRequest.getCommunityID());
        // TODO: Add code to handle community notification
    }

}
