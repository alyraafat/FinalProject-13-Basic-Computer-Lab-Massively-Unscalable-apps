package com.redditclone.user_service.controllers;

import com.redditclone.user_service.dtos.UserDTO;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.search.EmailSearchStrategy;
import com.redditclone.user_service.search.UserSearchStrategy;
import com.redditclone.user_service.search.UsernameSearchStrategy;
import com.redditclone.user_service.services.UserService;
import com.redditclone.user_service.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/get_all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(UserDTO.fromEntity(created));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(value = "X-User-Id", required = false) UUID userId, @RequestBody User user) {
        User updated = userService.updateUser(userId, user);
        return ResponseEntity.ok(UserDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(
            @RequestParam String keyword,
            @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId,
            @RequestParam(defaultValue = "username") String strategyType
    ) {
        List<UserDTO> results = userService.searchUsers(keyword, currentUserId, strategyType)
                .stream().map(UserDTO::fromEntity).toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/test_auth")
    public ResponseEntity<Object> testAuth() {
        return ResponseHandler.generateResponse("Test Auth", HttpStatus.OK, null);
    }

    @GetMapping("/get_emails")
    public ResponseEntity<List<String>> getUserEmails(
            @RequestParam("ids") List<UUID> ids
    ) {
        List<String> emails = userService.getEmailsByIds(ids);
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<String> getIdByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user.getId().toString());
    }

}
