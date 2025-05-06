package com.example.moderator.controller;

import com.example.moderator.model.Report;
import com.example.moderator.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Create a new report, RabbitMQ
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestParam UUID userReporting,
                                               @RequestParam UUID itemReported,
                                               @RequestParam String description,
                                               @RequestParam UUID communityId) {
        Report report = reportService.createReport(userReporting, itemReported, description, communityId);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    // Get a report by ID
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable UUID id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    // Get report IDs for a specific item
    @GetMapping("/item/{itemId}/ids")
    public ResponseEntity<List<UUID>> getReportIdsForItem(@PathVariable UUID itemId) {
        List<UUID> reportIds = reportService.getReportsForItem(itemId);
        return ResponseEntity.ok(reportIds);
    }

    // Get all reports by list of community IDs
    @PostMapping("/communities")
    public ResponseEntity<List<Report>> getReportsByCommunityIds(@RequestBody List<UUID> communityIds) {
        List<Report> reports = reportService.getAllReportsByCommunityIds(communityIds);
        return ResponseEntity.ok(reports);
    }

    // Mark multiple reports as handled
    @PostMapping("/mark-handled")
    public ResponseEntity<Void> markReportsAsHandled(@RequestBody List<UUID> reportIds) {
        reportService.markReportAsHandledMultiple(reportIds);
        return ResponseEntity.noContent().build();
    }

    // Delete a report by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportById(@PathVariable UUID id) {
        reportService.deleteReportById(id);
        return ResponseEntity.noContent().build();
    }
}
