package com.example.moderator.command.impl;

import com.example.moderator.command.Command;
import com.example.moderator.model.Report;
import com.example.moderator.repository.ModeratorRepository;
import com.example.moderator.service.ModeratorService;
import com.example.moderator.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


public class ViewReportsCommand implements Command<List<Report>> {

    private final ReportService reportService;
    private final ModeratorRepository moderatorRepository;
    private final UUID moderatorID;

    public ViewReportsCommand(ReportService reportService, ModeratorRepository moderatorRepository, UUID moderatorID) {
        this.reportService = reportService;
        this.moderatorRepository = moderatorRepository;
        this.moderatorID = moderatorID;
    }

    @Override
    public List<Report> execute() {
            List<UUID> communities = moderatorRepository.findCommunityIdsByUserId(moderatorID);
            return reportService.getAllReportsByCommunityIds(communities);
    }

//    public static class ReportRetrievalFailedException extends RuntimeException {
//        public ReportRetrievalFailedException(UUID communityId, Throwable cause) {
//            super("Failed to retrieve reports for community " + communityId + ": " + cause.getMessage(), cause);
//        }
//    }
}
