package com.redditclone.user_service.services;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User createUser(User user) {
        user.setId(null);
        user.setCreatedAt(Instant.now());
        user.setActivated(true);
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setActivated(updatedUser.isActivated());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }


    public List<User> searchUsers(String keyword, UUID currentUserId) {
        List<UUID> blockedUserIds = blockService.getBlockedUsers(currentUserId)
                .stream()
                .map(User::getId)
                .toList();

        return userRepository.searchUsersExcludingBlocked(keyword, blockedUserIds);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
