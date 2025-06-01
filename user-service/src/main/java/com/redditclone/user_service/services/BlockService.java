package com.redditclone.user_service.services;

import com.redditclone.user_service.exceptions.RedditAppException;
import com.redditclone.user_service.models.Block;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.BlockRepository;
import com.redditclone.user_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(BlockService.class);

    @Autowired
    public BlockService(BlockRepository blockRepository, UserRepository userRepository) {
        this.blockRepository = blockRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void blockUser(UUID userId, UUID blockedId) {
        if (userId.equals(blockedId)) {
            throw new IllegalArgumentException("Cannot block yourself");
        }

        User[] users = getBlockerAndBlocked(userId, blockedId);
        User blocker = users[0];
        User blocked = users[1];

        if (!blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            blockRepository.save(new Block(blocker, blocked));
            log.info("User {} successfully blocked user {}", userId, blockedId);
        }
    }

    @Transactional
    public void unblockUser(UUID userId, UUID blockedId) {
        User[] users = getBlockerAndBlocked(userId, blockedId);
        User blocker = users[0];
        User blocked = users[1];

        if (blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            blockRepository.deleteByBlockerAndBlocked(blocker, blocked);
            log.info("User {} successfully unblocked user {}", userId, blockedId);
        } else {
            throw new EntityNotFoundException("Block relationship does not exist");
        }
    }

    @Transactional(readOnly = true)
    public boolean isBlocked(UUID userId, UUID blockedId) {
        User[] users = getBlockerAndBlocked(userId, blockedId);
        User blocker = users[0];
        User blocked = users[1];

        return blockRepository.existsByBlockerAndBlocked(blocker, blocked);
    }

    @Transactional(readOnly = true)
    public List<User> getBlockedUsers(UUID userId) {
        User user = findUser(userId, "User not found");
        List<User> blockedUsers = blockRepository.findBlockedUsersByBlockerId(user.getId());
        log.info("User {} has blocked {} users", userId, blockedUsers.size());
        return blockedUsers;
    }

    @Transactional(readOnly = true)
    public List<User> getUsersThatBlocked(UUID userId) {
        User user = findUser(userId, "User not found");
        List<User> blockers = blockRepository.findBlockersByBlockedId(user.getId());
        log.info("User {} is blocked by {} users", userId, blockers.size());
        return blockers;
    }

    public User findUser(UUID userId, String errorMessage) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));
    }

    public User[] getBlockerAndBlocked(UUID userId, UUID blockedId) {
        User blocker = findUser(userId, "User not found");
        User blocked = findUser(blockedId, "User to block not found");
        return new User[]{blocker, blocked};
    }
}
