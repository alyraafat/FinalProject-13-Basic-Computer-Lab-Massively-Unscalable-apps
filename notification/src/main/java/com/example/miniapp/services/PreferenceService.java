package com.example.miniapp.services;

import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;

    public PreferenceService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public UserPreference getPreferences(String userId) {
        return preferenceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found"));
    }
}