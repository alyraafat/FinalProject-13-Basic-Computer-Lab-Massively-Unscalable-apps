package com.example.miniapp.models.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
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
