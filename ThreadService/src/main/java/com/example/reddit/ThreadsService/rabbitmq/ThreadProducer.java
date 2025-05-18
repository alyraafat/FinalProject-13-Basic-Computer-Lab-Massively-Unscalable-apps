package com.example.reddit.ThreadsService.rabbitmq;
import com.example.reddit.ThreadsService.dto.NotificationRequest;
import com.example.reddit.ThreadsService.enums.NotificationType;
import com.example.reddit.ThreadsService.models.Log;
import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.services.LogService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.reddit.ThreadsService.dto.ReportRequest;
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

    public void sendThreadNotificationRequest(Thread thread, String type) {
        UUID threadID = thread.getId();
        String title = thread.getTitle();
        List<Log> threadLogs = logService.getLogsByThread(threadID);
        // TODO: check if these userIds should be distinct
        List<UUID> userIds = threadLogs.stream().map(Log::getUserId).distinct().toList();

        String rawMessage = "Someone downvoted your post titled: " + title;
        if (type.equals("comment")) {
            rawMessage = "Someone added a new comment on the thread titled: " + title;
        } else if (type.equals("up")) {
            rawMessage = "Someone upvoted your post titled: " + title;
        }
        String senderName = thread.getTitle();
        NotificationRequest notificationRequest = new NotificationRequest(rawMessage, userIds, threadID, senderName);
        if (type.equals("up") || type.equals("down")) {
            notificationRequest.setType(NotificationType.USER_SPECIFIC);
        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.THREAD_EXCHANGE, RabbitMQConfig.THREAD_NOTIFICATION_ROUTING_KEY, notificationRequest);

        System.out.println("Thread notification request for thread: " + threadID + " sent by ThreadProducer");
        System.out.println(userIds.size() + " users will be notified");
    }
}
