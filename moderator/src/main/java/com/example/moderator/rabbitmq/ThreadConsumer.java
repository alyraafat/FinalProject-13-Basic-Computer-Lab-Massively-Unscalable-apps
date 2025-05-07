package com.example.moderator.rabbitmq;

import com.example.moderator.dto.ReportRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.REPORT_QUEUE)
public class ThreadConsumer {
    // TODO: Inject needed services to add ReportRequest processing logic

    @RabbitHandler
    public void process(ReportRequest reportRequest) {
        System.out.println("Received report request with thread id: " + reportRequest.getThreadId()
                           + " and content: " + reportRequest.getContent());
    }
}
