package com.example.miniapp.models.entity;

import com.example.miniapp.models.enums.NotificationPreference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "user_preferences")
public class UserPreference {

    @Id
    private UUID userId;

    private NotificationPreference preference = NotificationPreference.PUSH; // default

    private String userEmail;


    public UserPreference() {
    }

    public UserPreference(UUID userId) {
        this.userId = userId;
        this.preference = NotificationPreference.PUSH;
        this.userEmail = "";
    }

    public UserPreference(UUID userId, String userEmail) {
        this.userId = userId;
        this.preference = NotificationPreference.PUSH;
        this.userEmail = userEmail;
    }

    // Getters and setters
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