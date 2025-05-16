package com.example.miniapp.models.dto;

import com.example.miniapp.models.enums.NotificationPreference;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class PreferenceUpdateRequest {
    private UUID userId;
    private NotificationPreference preference;

    public PreferenceUpdateRequest() {
    }

    public PreferenceUpdateRequest(UUID userId, NotificationPreference preference) {
        this.userId = userId;
        this.preference = preference;
    }

}
