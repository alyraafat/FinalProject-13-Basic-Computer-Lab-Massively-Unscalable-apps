package com.redditclone.user_service.services;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.search.EmailSearchStrategy;
import com.redditclone.user_service.search.UserSearchStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class SearchStrategyService {
    private UserSearchStrategy userSearchStrategy;

    public SearchStrategyService(@Qualifier("email") UserSearchStrategy userSearchStrategy) {
        this.userSearchStrategy = userSearchStrategy;
    }

    public void setSearchStrategy(UserSearchStrategy userSearchStrategy) {
        this.userSearchStrategy = userSearchStrategy;
    }

    public List<User> searchUser(String keyword, UUID currentUserID) {
        return this.userSearchStrategy.search(keyword, currentUserID);
    }
}
