package com.redditclone.user_service.search;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.services.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component("email")
@RequiredArgsConstructor
public class EmailSearchStrategy implements UserSearchStrategy {

    private final UserRepository userRepository;
    private final BlockService blockService;

    @Override
    public List<User> search(String keyword, UUID currentUserId) {
        List<UUID> blockedIds = blockService.getBlockedUsers(currentUserId)
                .stream().map(User::getId).toList();
        return userRepository.searchByEmail(keyword, blockedIds);
    }
}
