package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.UserPreference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreferenceRepository  extends MongoRepository<UserPreference, String> {
    Optional<UserPreference> findByUserId(UUID userId);
}