package com.example.moderator.repository;

import com.example.moderator.model.Report;
import com.example.moderator.model.Report.Status;
import com.example.moderator.model.Report.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    // Find all reports by status (handled/unhandled)
    List<Report> findByStatus(Status status);

    // Find reports by item type (thread/comment)
    List<Report> findByItemType(ItemType itemType);

    // Find unhandled reports by item type
    List<Report> findByItemTypeAndStatus(ItemType itemType, Status status);

    // Find reports for a specific item (thread or comment)
    List<Report> findByItemReported(UUID itemReported);

    // Find reports made by a specific user
    List<Report> findByUserReporting(UUID userReporting);

    // Count unhandled reports
    long countByStatus(Status status);

    // Update query to mark a single report as handled
    @Modifying
    @Query("UPDATE Report r SET r.status = 'HANDLED' WHERE r.id = ?1")
    int markReportAsHandled(UUID reportId);

    // Find reports for multiple items at once
    List<Report> findByItemReportedIn(List<UUID> itemIds);

    // In ReportRepository.java
    @Modifying
    @Transactional
    void deleteById(UUID reportId);
}
