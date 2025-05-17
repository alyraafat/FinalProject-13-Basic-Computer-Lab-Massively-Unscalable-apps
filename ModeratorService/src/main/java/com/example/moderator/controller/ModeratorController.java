package com.example.moderator.controller;

import com.example.moderator.model.Moderator;
import com.example.moderator.model.Report;
import com.example.moderator.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    // ========== Basic Moderator Management ==========

    @PostMapping
    public ResponseEntity<Object> addModerator(@RequestParam UUID userId, @RequestParam UUID communityId, @RequestHeader(value = "X-User-Id", required = false) UUID moderatorId) {
        Moderator moderator = moderatorService.addModerator(userId, communityId, moderatorId);
        return ResponseEntity.ok(moderator);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeModerator(@RequestParam UUID userId, @RequestParam UUID communityId, @RequestHeader(value = "X-User-Id", required = false) UUID moderatorId) {
        moderatorService.removeModerator(userId, communityId, moderatorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<List<Moderator>> getModeratorsForCommunity(@PathVariable UUID communityId) {
        List<Moderator> moderators = moderatorService.getModeratorsForCommunity(communityId);
        return ResponseEntity.ok(moderators);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isModerator(@RequestParam UUID userId, @RequestParam UUID communityId) {
        boolean isMod = moderatorService.isModerator(userId, communityId);
        return ResponseEntity.ok(isMod);
    }

    // ========== Moderator Privileged Operations ==========

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> viewReports(@RequestHeader(value = "X-User-Id", required = false) UUID moderatorId) {
        List<Report> reports = moderatorService.viewReports(moderatorId);
        return ResponseEntity.ok(reports);
    }

    //RabbitMQ
    @PostMapping("/comment/remove")
    public ResponseEntity<Void> removeComment(@RequestHeader(value = "X-User-Id", required = false) UUID moderatorId,
                                              @RequestParam UUID communityId,
                                              @RequestParam UUID threadId,
                                              @RequestParam UUID commentId) {
        moderatorService.removeComment(moderatorId, communityId, threadId, commentId);
        return ResponseEntity.noContent().build();
    }

    //RabbitMQ
    @PostMapping("/user/ban")
    public ResponseEntity<Void> banUser(@RequestHeader(value = "X-User-Id", required = false) UUID moderatorId,
                                        @RequestParam UUID communityId,
                                        @RequestParam UUID userId) {
        moderatorService.banUser(moderatorId, communityId, userId);
        return ResponseEntity.noContent().build();
    }

    //RabbitMQ
    @PostMapping("/user/unban")
    public ResponseEntity<Void> unbanUser(@RequestHeader(value = "X-User-Id", required = false) UUID moderatorId,
                                          @RequestParam UUID communityId,
                                          @RequestParam UUID userId) {
        moderatorService.unbanUser(moderatorId, communityId, userId);
        return ResponseEntity.noContent().build();
    }
}