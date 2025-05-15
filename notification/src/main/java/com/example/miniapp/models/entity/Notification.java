package com.example.miniapp.models.entity;

import com.example.miniapp.models.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private UUID id;

    private NotificationType type;
    private String senderId;
    private String title;
    private String message;
    private Instant createdAt;
    private String senderName;
    @Field("receivers_id")
    private List<UUID> receiversId;

    // Getters and Setters

    public Notification(NotificationType type, String senderId, String title, String message, String senderName, List<UUID> receiversId) {
        this.type = type;
        this.senderId = senderId;
        this.title = title;
        this.message = message;
        this.senderName = senderName;
        this.receiversId = receiversId;
        this.createdAt = Instant.now();
    }

}