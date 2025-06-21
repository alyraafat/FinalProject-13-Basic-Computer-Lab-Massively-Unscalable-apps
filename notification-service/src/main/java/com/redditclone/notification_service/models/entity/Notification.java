package com.redditclone.notification_service.models.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.redditclone.notification_service.models.enums.NotificationType;
import com.redditclone.notification_service.services.Factory.impl.CommunityNotifier;
import com.redditclone.notification_service.services.Factory.impl.ThreadNotifier;
import com.redditclone.notification_service.services.Factory.impl.UserNotifier;
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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommunityNotifier.class, name = "COMMUNITY"),
        @JsonSubTypes.Type(value = ThreadNotifier.class, name = "THREAD"),
        @JsonSubTypes.Type(value = UserNotifier.class, name = "USER_SPECIFIC")
})
public abstract class Notification {

    @Id
    private String id;

    private NotificationType type;
    private String senderId;
    private String title;
    private String message;
    private Instant createdAt;
    private String senderName;
    @Field("receivers_id")
    private List<UUID> receiversId;


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