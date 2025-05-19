package com.example.miniapp.models.entity;

import com.example.miniapp.models.enums.NotificationPreference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Setter
@Getter
@Document(collection = "user_preferences")
public class UserPreference {

    @Id
    private String id;

    @Indexed(unique = true)
    private UUID userId;


    private NotificationPreference preference = NotificationPreference.PUSH; // default

    private String userEmail;


    public UserPreference() {
    }

    public UserPreference(UUID userId, String email) {
        this.userId = userId;
        this.userEmail = email;
    }

    public UserPreference(String id, UUID userId, NotificationPreference preference, String email) {
        this.id = id;
        this.userId = userId;
        this.preference = preference;
        this.userEmail = email;
    }

}