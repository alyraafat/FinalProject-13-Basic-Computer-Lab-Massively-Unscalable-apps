package com.redditclone.communities_service.dto;

import java.util.List;
import java.util.UUID;

public class CommunityNotificationRequest {
    private UUID communityID;
    private String communityName;
    private List<UUID> memberIds;

    public CommunityNotificationRequest() {}
    public CommunityNotificationRequest(UUID communityID, String communityName, List<UUID> memberIds) {
        this.communityID = communityID;
        this.communityName = communityName;
        this.memberIds = memberIds;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<UUID> memberIds) {
        this.memberIds = memberIds;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public UUID getCommunityID() {
        return communityID;
    }

    public void setCommunityID(UUID communityID) {
        this.communityID = communityID;
    }
}
