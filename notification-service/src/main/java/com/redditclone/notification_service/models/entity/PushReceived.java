package com.redditclone.notification_service.models.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Document(collection = "push_receives")
public class PushReceived {

    @Id
    private String id;
    private String title;
    private String message;
    private Instant createdAt;
    private UUID receiverId;


    public PushReceived(String title, String message, Instant createdAt, UUID receiverId) {
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.receiverId = receiverId;
    }


}
