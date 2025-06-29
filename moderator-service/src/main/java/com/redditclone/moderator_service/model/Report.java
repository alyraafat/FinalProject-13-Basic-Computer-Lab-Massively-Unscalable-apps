package com.redditclone.moderator_service.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "reports")
public class Report {

    public enum Status {
        HANDLED,
        UNHANDLED
    }

    @Id
    @GeneratedValue
    private UUID id;  // primary key

    @Column(name = "user_reporting", nullable = false)
    private UUID userReporting;  // UUID of user who made the report

    @Column(name = "item_reported", nullable = false)
    private UUID itemReported;  // UUID of the item being reported

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.UNHANDLED;  // default to UNHANDLED

    @Column(name = "report_description", length = 1000)
    private String reportDescription;  // description of the report

    @Column(name = "community_id")
    private UUID communityId;

    // Constructors
    public Report() {
    }

    public Report(UUID userReporting, UUID itemReported, String reportDescription, UUID communityId) {
        this.userReporting = userReporting;
        this.itemReported = itemReported;
        ;
        this.reportDescription = reportDescription;
        this.status = Status.UNHANDLED; // default status
        this.communityId = communityId;
    }

    public UUID getCommunityId() {
        return communityId;
    }

    public void setCommunityId(UUID communityId) {
        this.communityId = communityId;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserReporting() {
        return userReporting;
    }

    public void setUserReporting(UUID userReporting) {
        this.userReporting = userReporting;
    }

    public UUID getItemReported() {
        return itemReported;
    }

    public void setItemReported(UUID itemReported) {
        this.itemReported = itemReported;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userReporting=" + userReporting +
                ", itemReported=" + itemReported +
                ", community_id=" + communityId +
                ", status=" + status +
                ", reportDescription='" + reportDescription + '\'' +
                '}';
    }
}
