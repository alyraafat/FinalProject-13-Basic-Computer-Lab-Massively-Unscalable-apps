package com.example.moderator.dto;

import java.util.UUID;

public class UnbanRequest {
    private UUID userID;
    private UUID communityID;

    public UnbanRequest(UUID userID, UUID communityID) {
        this.userID = userID;
        this.communityID = communityID;
    }

    public UnbanRequest() {}

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
