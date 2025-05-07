package com.example.reddit.ThreadsService.rabbitmq;

import com.example.reddit.ThreadsService.dto.DeleteCommentRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.DELETE_COMMENT_QUEUE)
public class ModeratorConsumer {
    // TODO: Inject needed services to add DeleteCommentRequest processing logic

    @RabbitHandler
    public void process(DeleteCommentRequest deleteCommentRequest) {
        System.out.println("Received comment deletion request with thread id: " + deleteCommentRequest.getThreadId()
                           + "\nand comment id: " + deleteCommentRequest.getCommentId()
                           + "\nby moderator: " + deleteCommentRequest.getModeratorId());
    }
}
