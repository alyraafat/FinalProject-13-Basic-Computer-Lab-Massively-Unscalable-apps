package com.example.miniapp.models.dto;

import com.example.miniapp.models.enums.NotificationPreference;

import java.util.UUID;

public class PreferenceUpdateRequest {
    private UUID userId;
    private NotificationPreference preference;
    private String userEmail;  // optional

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public NotificationPreference getPreference() {
        return preference;
    }

    public void setPreference(NotificationPreference preference) {
        this.preference = preference;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
