package com.example.reddit.ThreadsService.rabbitmq;

import com.example.reddit.ThreadsService.dto.ReportRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ThreadProducer {
    private final RabbitTemplate rabbitTemplate;

    public ThreadProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendReportRequest(UUID threadId, String content) {
        ReportRequest reportRequest = new ReportRequest(threadId, content);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "thread.reportRequest", reportRequest);

        System.out.println("Report request for thread " + threadId + " sent by ThreadProducer with content " + content);
    }
}
