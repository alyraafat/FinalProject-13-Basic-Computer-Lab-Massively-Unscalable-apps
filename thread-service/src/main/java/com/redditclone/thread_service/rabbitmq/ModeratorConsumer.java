package com.redditclone.thread_service.rabbitmq;

import com.redditclone.thread_service.dto.DeleteCommentRequest;
import com.redditclone.thread_service.services.ThreadService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.DELETE_COMMENT_QUEUE)
public class ModeratorConsumer {
    final ThreadService threadService;
    @Autowired

    public ModeratorConsumer(ThreadService threadService)
    {
        this.threadService=threadService;
    }
    // TODO: Inject needed services to add DeleteCommentRequest processing logic

    @RabbitHandler
    public void process(DeleteCommentRequest deleteCommentRequest) {

        System.out.println("Received comment deletion request for Comment id: " + deleteCommentRequest.getCommentId());
        threadService.removeComment(deleteCommentRequest.getThreadId(),deleteCommentRequest.getCommentId());
    }
}
