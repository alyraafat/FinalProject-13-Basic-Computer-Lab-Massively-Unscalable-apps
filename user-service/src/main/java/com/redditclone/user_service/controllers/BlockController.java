package com.redditclone.user_service.controllers;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.services.BlockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/block/{userId}")
public class BlockController {
    private final BlockService blockService;

    @Autowired
    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/{blockedId}")
    public ResponseEntity<Void> blockUser(
            @PathVariable UUID userId,
            @PathVariable UUID blockedId
    ) {
        try {
            blockService.blockUser(userId, blockedId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{blockedId}")
    public ResponseEntity<Void> unblockUser(
            @PathVariable UUID userId,
            @PathVariable UUID blockedId
    ) {
        try {
            blockService.unblockUser(userId, blockedId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{blockedId}")
    public ResponseEntity<Boolean> isBlocked(
            @PathVariable UUID userId,
            @PathVariable UUID blockedId
    ) {
        try {
            boolean blocked = blockService.isBlocked(userId, blockedId);
            return ResponseEntity.ok(blocked);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getBlockedUsers(
            @PathVariable UUID userId
    ) {
        try {
            List<User> blocked = blockService.getBlockedUsers(userId);
            return ResponseEntity.ok(blocked);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/allblocks")
    public ResponseEntity<List<String>> getAllBlockedOrBlockedByUsers(@PathVariable UUID userId) {
        try{
            List<String> blocksUUID = new ArrayList<>();
            List<User> blocked = blockService.getBlockedUsers(userId);
            List<User> blockedBy = blockService.getUsersThatBlocked(userId);
            for (User user : blockedBy) {
                blocksUUID.add(user.getId().toString());
            }
            for (User user : blocked) {
                blocksUUID.add(user.getId().toString());
            }
            System.out.println(blocksUUID);
            return ResponseEntity.ok(blocksUUID);
        }
        catch(EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
