package com.example.moderator.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "reports")
public class Report {

    public enum ItemType {
        THREAD,
        COMMENT
    }

    public enum Status {
        HANDLED,
        UNHANDLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;  // primary key

    @Column(name = "user_reporting", nullable = false)
    private UUID userReporting;  // UUID of user who made the report

    @Column(name = "item_reported", nullable = false)
    private UUID itemReported;  // UUID of the item being reported

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType itemType;  // whether it's a thread or comment

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.UNHANDLED;  // default to UNHANDLED

    @Column(name = "report_description", length = 1000)
    private String reportDescription;  // description of the report

    // Constructors
    public Report() {
    }

    public Report(UUID userReporting, UUID itemReported, ItemType itemType, String reportDescription) {
        this.id = UUID.randomUUID();
        this.userReporting = userReporting;
        this.itemReported = itemReported;
        this.itemType = itemType;
        this.reportDescription = reportDescription;
        this.status = Status.UNHANDLED; // default status
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

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
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
                ", itemType=" + itemType +
                ", status=" + status +
                ", reportDescription='" + reportDescription + '\'' +
                '}';
    }
}
