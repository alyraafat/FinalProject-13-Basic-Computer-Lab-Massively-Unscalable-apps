package com.redditclone.moderator_service.dto;

import java.util.UUID;

public class BanRequest {
    private UUID userID;
    private UUID communityID;

    public BanRequest(UUID userID, UUID communityID) {
        this.userID = userID;
        this.communityID = communityID;
    }

    public BanRequest() {
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getCommunityID() {
        return communityID;
    }

    public void setCommunityID(UUID communityID) {
        this.communityID = communityID;
    }
}
