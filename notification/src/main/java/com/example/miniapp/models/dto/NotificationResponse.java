package com.example.miniapp.models.dto;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "notification_response")
public class NotificationResponse {

    // PRIMARY KEY
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}