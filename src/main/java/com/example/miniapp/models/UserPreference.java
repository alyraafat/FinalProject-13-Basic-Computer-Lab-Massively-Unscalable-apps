package com.example.miniapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class UserPreference {
    @Id
    private String userId;

    @Enumerated(EnumType.STRING)
    private DeliveryChannel primaryChannel;

    public UserPreference(String userId) {
        this.userId = userId;
    }

    public UserPreference() {
        this.primaryChannel = null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setPrimaryChannel(DeliveryChannel deliveryChannel) {
    }

    public DeliveryChannel getPrimaryChannel() {
        return primaryChannel;
    }
}