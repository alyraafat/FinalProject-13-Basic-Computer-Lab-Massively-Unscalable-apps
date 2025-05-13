package com.example.reddit.ThreadsService.rabbitmq;
import com.example.reddit.ThreadsService.dto.NotificationRequest;
import com.example.reddit.ThreadsService.dto.ThreadNotificationRequest;
import com.example.reddit.ThreadsService.models.Log;
import com.example.reddit.ThreadsService.services.LogService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.reddit.ThreadsService.dto.ReportRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ThreadProducer {
    private final RabbitTemplate rabbitTemplate;
    private final LogService logService;

    public ThreadProducer(RabbitTemplate rabbitTemplate, LogService logService) {
        this.rabbitTemplate = rabbitTemplate;
        this.logService = logService;
    }

    public void sendReportRequest(UUID userReporting, UUID itemReported, String reportDescription, UUID communityId) {
        ReportRequest reportRequest = new ReportRequest(userReporting, itemReported, reportDescription, communityId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.THREAD_EXCHANGE, RabbitMQConfig.REPORT_REQUEST_ROUTING_KEY, reportRequest);

        System.out.println("Report request for community: " + reportRequest.getCommunityId()
                           + " sent by ThreadProducer with description: " + reportRequest.getReportDescription());
    }

    public void sendThreadNotificationRequest(UUID threadId) {
        List<Log> threadLogs = logService.getLogsByThread(threadId);
        // TODO: check if these userIds should be distinct
        List<UUID> userIds = threadLogs.stream().map(Log::getUserId).distinct().toList();

        String rawMessage = "You have a notification from thread";
        String senderName = "Karim";
        NotificationRequest notificationRequest = new NotificationRequest(rawMessage, userIds, threadId, senderName);
        rabbitTemplate.convertAndSend(RabbitMQConfig.THREAD_EXCHANGE, RabbitMQConfig.THREAD_NOTIFICATION_ROUTING_KEY, notificationRequest);

        System.out.println("Thread notification request for thread: " + threadId + " sent by ThreadProducer");
        System.out.println(userIds.size() + " users will be notified");
    }
}
