package com.example.moderator.rabbitmq;

import com.example.moderator.dto.DeleteCommentRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ModeratorProducer {
    private final RabbitTemplate rabbitTemplate;

    public ModeratorProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDeleteCommentRequest(UUID commentId) {
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.THREAD_EXCHANGE,
                RabbitMQConfig.DELETE_COMMENT_REQUEST_ROUTING_KEY, deleteCommentRequest);

        System.out.println("Sent delete request for comment id: " + commentId);
    }
}
