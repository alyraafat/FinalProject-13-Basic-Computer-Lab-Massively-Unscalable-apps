package com.example.moderator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ReportRequest {
    private UUID userReporting;
    private UUID itemReported;
    private String reportDescription;
    private UUID communityId;

    public ReportRequest() {}
    public ReportRequest(UUID userReporting, UUID itemReported, String reportDescription, UUID communityId) {
        this.userReporting = userReporting;
        this.itemReported = itemReported;
        this.reportDescription = reportDescription;
        this.communityId = communityId;
    }

    public UUID getUserReporting() {
        return userReporting;
    }

    public UUID getItemReported() {
        return itemReported;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public UUID getCommunityId() {
        return communityId;
    }
}
