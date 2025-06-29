package com.redditclone.thread_service.dto;

import java.util.List;
import java.util.UUID;

public class ThreadNotificationRequest {
    private UUID threadId;
    private List<UUID> userIds;

    public ThreadNotificationRequest() {}
    public ThreadNotificationRequest(UUID threadId, List<UUID> userIds) {
        this.threadId = threadId;
        this.userIds = userIds;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public void setThreadId(UUID threadId) {
        this.threadId = threadId;
    }

    public List<UUID> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<UUID> userIds) {
        this.userIds = userIds;
    }
}
