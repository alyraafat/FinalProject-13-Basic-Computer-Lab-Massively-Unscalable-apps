package com.redditclone.user_service.search;

import com.redditclone.user_service.models.User;

import java.util.List;
import java.util.UUID;

public interface UserSearchStrategy {
    List<User> search(String keyword, UUID currentUserId);
}

