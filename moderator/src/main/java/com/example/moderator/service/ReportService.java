package com.example.moderator.service;

import com.example.moderator.model.Report;
import com.example.moderator.model.Report.Status;
import com.example.moderator.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // Create a new report
    @Transactional
    public Report createReport(UUID reportingUserId, UUID itemReported, String description, UUID community_ID) {
        Report report = new Report(reportingUserId, itemReported, description, community_ID);
        return reportRepository.save(report);
    }

    // Get a single report by ID
    public Report getReportById(UUID reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));
    }

    public List<Report> getAllReportsByCommunityIds(List<UUID> communityIds){
        return  reportRepository.findByCommunityIdIn(communityIds);
    }


    // Get reports for a specific item
    public List<UUID> getReportsForItem(UUID itemId) {
        return reportRepository.findIdsByItemReported(itemId);
    }




//    // Get reports made by a specific user
//    public List<Report> getReportsByUser(UUID userId) {
//        return reportRepository.findByUserReporting(userId);
//    }
//
//    // Count reports by status
//    public long countReportsByStatus(Status status) {
//        return reportRepository.countByStatus(status);
//    }
//
//    // Mark a report as handled
//    @Transactional
//    public void markReportAsHandled(UUID reportId) {
//        int updatedCount = reportRepository.markReportAsHandled(reportId);
//        if (updatedCount == 0) {
//            throw new ReportNotFoundException(reportId);
//        }
//    }

    @Transactional
    public void markReportAsHandledMultiple(List<UUID> reportId) {
        int updatedCount = reportRepository.markReportsAsHandledMultiple(reportId);
        if (updatedCount == 0) {
            throw new ReportNotFoundException(reportId.get(0));
        }
    }


    // In ReportService.java
    @Transactional
    public void deleteReportById(UUID reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new ReportNotFoundException(reportId);
        }
        reportRepository.deleteById(reportId);
    }

    // Custom exception for report not found
    public static class ReportNotFoundException extends RuntimeException {
        public ReportNotFoundException(UUID reportId) {
            super("Report not found with ID: " + reportId);
        }
    }
}
