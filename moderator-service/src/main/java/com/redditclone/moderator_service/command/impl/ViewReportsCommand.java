package com.redditclone.moderator_service.command.impl;

import com.redditclone.moderator_service.command.Command;
import com.redditclone.moderator_service.model.Report;
import com.redditclone.moderator_service.repository.ModeratorRepository;
import com.redditclone.moderator_service.service.ReportService;

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
