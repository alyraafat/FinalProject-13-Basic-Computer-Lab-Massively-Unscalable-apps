package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommunityNotifier extends Notification {
    public CommunityNotifier() { super(); }
    public CommunityNotifier(String senderId, String title, String message, String senderName, List<UUID> receiversId) {
        super(NotificationType.COMMUNITY, senderId, title, message, senderName, receiversId);
    }
}