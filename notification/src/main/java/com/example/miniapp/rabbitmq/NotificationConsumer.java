package com.example.miniapp.rabbitmq;



import com.example.miniapp.models.MemberDTO;
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
    }

}
