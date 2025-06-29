package com.redditclone.communities_service.controllers;

import com.redditclone.communities_service.models.Community;
import com.redditclone.communities_service.models.CommunityCreateRequest;
import com.redditclone.communities_service.models.CommunityThread;
import com.redditclone.communities_service.services.CommunityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable UUID id) {
        return communityService.getCommunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // check if user is banned from community
    @GetMapping("/{id}/is-banned/{userId}")
    public boolean isUserBanned(@PathVariable UUID id, @PathVariable UUID userId) {
        boolean isBanned = communityService.isUserBanned(id, userId);
        return isBanned;
    }

    @PostMapping
    public ResponseEntity<Community> createCommunity(
            @Valid @RequestBody CommunityCreateRequest req, @RequestHeader("X-User-Id") UUID userId
    ) {

        Community community = communityService.createCommunity(
                req.getName(),
                req.getTopicId(),
                req.getDescription(),
                userId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(community);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Community> updateCommunity(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) UUID topic) {

        Community updatedCommunity = communityService.updateCommunity(id, name, description, topic);
        return ResponseEntity.ok(updatedCommunity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable UUID id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<Community>> getCommunitiesByTopicId(@PathVariable UUID topicId) {
        return ResponseEntity.ok(communityService.getCommunitiesByTopicId(topicId));
    }

    @GetMapping("/moderator/{moderatorId}")
    public ResponseEntity<List<Community>> getCommunitiesByModeratorId(@PathVariable UUID moderatorId) {
        return ResponseEntity.ok(communityService.getCommunitiesByModeratorId(moderatorId));
    }

    @GetMapping("/member")
    public ResponseEntity<List<Community>> getCommunitiesByMemberId(@RequestHeader("X-User-Id") UUID memberId) {
        return ResponseEntity.ok(communityService.getCommunitiesByMemberId(memberId));
    }

    @PutMapping("/{communityId}/thread/{threadId}")
    public ResponseEntity<String> addThread(
            @PathVariable UUID communityId,
            @PathVariable UUID threadId
    ) {
        communityService.addThread(communityId, threadId);
        return ResponseEntity.ok("Thread added successfully");
    }

   @PutMapping("/{communityId}/moderators/{userId}")
   public ResponseEntity<Community> addModerator(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.addModerator(communityId, userId));
   }

   @DeleteMapping("/{communityId}/moderators/{userId}")
   public ResponseEntity<Community> removeModerator(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.removeModerator(communityId, userId));
   }

   @PutMapping("/{communityId}/members")
   public ResponseEntity<Community> addMember(@PathVariable UUID communityId, @RequestHeader("X-User-Id") UUID userId) {
       return ResponseEntity.ok(communityService.addMember(communityId, userId));
   }

   @DeleteMapping("/{communityId}/members")
   public ResponseEntity<Community> removeMember(@PathVariable UUID communityId, @RequestHeader("X-User-Id") UUID userId) {
       return ResponseEntity.ok(communityService.removeMember(communityId, userId));
   }

   @PutMapping("/{communityId}/ban/{userId}")
   public ResponseEntity<Community> banUser(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.banUser(communityId, userId));
   }

   @DeleteMapping("/{communityId}/ban/{userId}")
   public ResponseEntity<Community> unbanUser(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.unbanUser(communityId, userId));
   }


    /**
     * GET /communities/by-members
     * Returns all communities sorted by member count (descending).
     */
    @GetMapping("/by-members")
    public ResponseEntity<List<Community>> byMemberCount() {
        List<Community> sorted = communityService.getCommunitiesByMemberCountDesc();
        return ResponseEntity.ok(sorted);
    }

    /**
     * GET /communities/{id}/threads/newest
     * Returns all threads for the community, sorted by creation date (newest first).
     */
    @GetMapping("/{id}/threads/newest")
    public ResponseEntity<List<CommunityThread>> getThreadsNewest(@PathVariable("id") UUID communityId) {
        List<CommunityThread> threads = communityService.getCommunityThreadsByDate(communityId);
        return ResponseEntity.ok(threads);
    }

    /**
     * GET /communities/{id}/threads/top
     * Returns all threads for the community, sorted by upvotes (highest first).
     */
    @GetMapping("/{id}/threads/top")
    public ResponseEntity<List<CommunityThread>> getThreadsTop(@PathVariable("id") UUID communityId) {
        List<CommunityThread> threads = communityService.getCommunityThreadsByTop(communityId);
        return ResponseEntity.ok(threads);
    }

}