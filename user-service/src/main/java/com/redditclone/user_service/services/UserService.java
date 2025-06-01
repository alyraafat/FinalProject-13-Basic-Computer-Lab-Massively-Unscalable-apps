package com.redditclone.user_service.services;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.search.EmailSearchStrategy;
import com.redditclone.user_service.search.UserSearchStrategy;
import com.redditclone.user_service.search.UsernameSearchStrategy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BlockService blockService;
    private final SearchStrategyService searchStrategyService;
    private final UsernameSearchStrategy usernameSearchStrategy;
    private final EmailSearchStrategy emailSearchStrategy;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public User createUser(User user) {
        log.info("Creating user with username: {}", user.getUsername());
        user.setId(null);
        user.setCreatedAt(Instant.now());
        user.setActivated(true);
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
        if (updatedUser.getBio() != null) user.setBio(updatedUser.getBio());
        if (updatedUser.getFullName() != null) user.setFullName(updatedUser.getFullName());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String keyword, UUID currentUserId, String strategyType) {
        UserSearchStrategy userSearchStrategy;
        if (strategyType.equalsIgnoreCase("username")) {
            userSearchStrategy = usernameSearchStrategy;
        } else if (strategyType.equalsIgnoreCase("email")) {
            userSearchStrategy = emailSearchStrategy;
        } else {
            throw new IllegalArgumentException("Unknown strategy type: " + strategyType);
        }
        log.info("Searching users with keyword: '{}' using strategy: {}", keyword, userSearchStrategy.getClass().getSimpleName());
        searchStrategyService.setSearchStrategy(userSearchStrategy);
        return searchStrategyService.searchUser(keyword, currentUserId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public List<String> getEmailsByIds(List<UUID> userIDs) {
        log.debug("Fetching emails for {} user IDs", userIDs.size());
        return userRepository.findEmailsByIds(userIDs);
    }

    public User getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }
}
