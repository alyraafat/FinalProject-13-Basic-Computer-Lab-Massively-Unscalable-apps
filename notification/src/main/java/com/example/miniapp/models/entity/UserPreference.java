package com.example.miniapp.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "user_preferences")
public class UserPreference {

    @Id
    private UUID userId;

    private boolean emailNotifications;
    private boolean pushNotifications;
    private String userEmail;

    public UserPreference() {
    }
    public UserPreference(UUID userId) {
        this.userId = userId;
        emailNotifications = false;
        pushNotifications = true;
        userEmail = "";
    }

    public UserPreference(UUID userId, String userEmail) {
        this.userId = userId;
        emailNotifications = false;
        pushNotifications = true;
        this.userEmail = userEmail;
    }

    // Getters and setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}