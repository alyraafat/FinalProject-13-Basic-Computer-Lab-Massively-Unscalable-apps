//package com.example.moderator.command.impl;
//
//import com.example.moderator.command.Command;
//import com.example.moderator.model.Report;
//import com.example.moderator.service.ReportService;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.UUID;
//
//@Component
//public class ViewReportsCommand implements Command<List<Report>> {
//
//    private final ReportService reportService;
//
//    private UUID communityId;
//    private Report.Status status;
//    private Integer limit;
//
//    public ViewReportsCommand(ReportService reportService) {
//        this.reportService = reportService;
//    }
//
//    public ViewReportsCommand prepare(UUID communityId, Report.Status status, Integer limit) {
//        this.communityId = communityId;
//        this.status = status;
//        this.limit = limit;
//        return this;
//    }
//
//    @Override
//    public List<Report> execute() {
//        try {
//            if (limit != null) {
//                return reportService.getReportsByCommunityAndStatusWithLimit(communityId, status, limit);
//            }
//            return reportService.getReportsByCommunityAndStatus(communityId, status);
//        } catch (Exception e) {
//            throw new ReportRetrievalFailedException(communityId, e);
//        }
//    }
//
//    public static class ReportRetrievalFailedException extends RuntimeException {
//        public ReportRetrievalFailedException(UUID communityId, Throwable cause) {
//            super("Failed to retrieve reports for community " + communityId + ": " + cause.getMessage(), cause);
//        }
//    }
//}
