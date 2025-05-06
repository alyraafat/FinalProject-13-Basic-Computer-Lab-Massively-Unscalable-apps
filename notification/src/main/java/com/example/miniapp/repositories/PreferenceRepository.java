package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<UserPreference, String> {
}