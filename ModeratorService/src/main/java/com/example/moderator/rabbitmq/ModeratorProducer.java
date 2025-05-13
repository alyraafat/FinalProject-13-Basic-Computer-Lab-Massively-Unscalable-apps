package com.example.moderator.rabbitmq;

import com.example.moderator.dto.BanRequest;
import com.example.moderator.dto.DeleteCommentRequest;
import com.example.moderator.dto.UnbanRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ModeratorProducer {
    private final RabbitTemplate rabbitTemplate;

    public ModeratorProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDeleteCommentRequest(UUID commentId, UUID threadId) {
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, threadId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.THREAD_EXCHANGE,
                RabbitMQConfig.DELETE_COMMENT_REQUEST_ROUTING_KEY, deleteCommentRequest);

        System.out.println("Sent delete request for comment id: " + commentId);
    }

    public void sendBanMemberRequest(BanRequest banRequest) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.COMMUNITY_EXCHANGE,
                RabbitMQConfig.COMMUNITY_BAN_ROUTING, banRequest);
        System.out.println("Sent ban member request for request, userID: " + banRequest.getUserID() + "communityID: " + banRequest.getCommunityID());
    }

    public void sendUnBanMemberRequest(UnbanRequest unbanRequest) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.COMMUNITY_EXCHANGE,
                RabbitMQConfig.COMMUNITY_UNBAN_ROUTING, unbanRequest);
        System.out.println("Sent unban member request for request, userID: " + unbanRequest.getUserID() + "communityID: " + unbanRequest.getCommunityID());
    }
}
