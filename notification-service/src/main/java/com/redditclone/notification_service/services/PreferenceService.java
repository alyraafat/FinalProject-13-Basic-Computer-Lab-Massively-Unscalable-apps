package com.redditclone.notification_service.services;

import com.redditclone.notification_service.models.entity.UserPreference;
import com.redditclone.notification_service.repositories.PreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;

    public PreferenceService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public UserPreference getPreferences(UUID userId) {
        return preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found"));
    }
}