package com.redditclone.moderator_service.repository;

import com.redditclone.moderator_service.model.Report;
import com.redditclone.moderator_service.model.Report.Status;
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

    // Find reports by item type (thread/comment

    // Find reports for a specific item (thread or comment)
    List<Report> findByItemReported(UUID itemReported);

    //Find ids of reports that belong to an item.
    @Query("SELECT r.id FROM Report r WHERE r.itemReported = ?1")
    List<UUID> findIdsByItemReported(UUID itemReported);


    // Find reports made by a specific user
    List<Report> findByUserReporting(UUID userReporting);

    // Count unhandled reports
    long countByStatus(Status status);

    // Update query to mark a single report as handled
    @Modifying
    @Query("UPDATE Report r SET r.status = 'HANDLED' WHERE r.id = ?1")
    int markReportAsHandled(UUID reportId);

    @Modifying
    @Query("UPDATE Report r SET r.status = 'HANDLED' WHERE r.id IN ?1")
    int markReportsAsHandledMultiple(List<UUID> reportIds);

    // Find reports for multiple items at once
    List<Report> findByItemReportedIn(List<UUID> itemIds);

    //Find reports by list of community IDs
    List<Report> findByCommunityIdIn(List<UUID> communityId);

    // In ReportRepository.java
    @Modifying
    @Transactional
    void deleteById(UUID reportId);
}
