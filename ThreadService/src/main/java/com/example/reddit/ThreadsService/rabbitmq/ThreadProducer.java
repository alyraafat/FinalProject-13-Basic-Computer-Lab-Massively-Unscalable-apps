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

    public void sendReportRequest(UUID userReporting, UUID itemReported, String reportDescription, UUID communityId) {
        ReportRequest reportRequest = new ReportRequest(userReporting, itemReported, reportDescription, communityId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.THREAD_EXCHANGE, RabbitMQConfig.REPORT_REQUEST_ROUTING_KEY, reportRequest);

        System.out.println("Report request for community: " + reportRequest.getCommunityId()
                           + " sent by ThreadProducer with description: " + reportRequest.getReportDescription());
    }
}
