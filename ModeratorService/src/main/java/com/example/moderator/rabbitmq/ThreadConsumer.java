package com.example.moderator.rabbitmq;

import com.example.moderator.dto.ReportRequest;
import com.example.moderator.service.ReportService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.REPORT_QUEUE)
public class ThreadConsumer {

    private final ReportService reportService;

    public ThreadConsumer(ReportService reportService) {
        this.reportService = reportService;
    }

    @RabbitHandler
    public void process(ReportRequest reportRequest) {
        reportService.createReport(
                reportRequest.getUserReporting(),
                reportRequest.getItemReported(),
                reportRequest.getReportDescription(),
                reportRequest.getCommunityId()
        );
        System.out.println("Received report request with community id: " + reportRequest.getCommunityId()
                + " and description: " + reportRequest.getReportDescription());
    }
}
