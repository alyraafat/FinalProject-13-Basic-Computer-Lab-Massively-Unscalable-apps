package com.example.reddit.CommunitiesService.controllers;

import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/communities")
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

    @PostMapping
    public ResponseEntity<Community> createCommunity(@RequestBody Community community) {
        return ResponseEntity.ok(communityService.createCommunity(community));
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

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Community>> getCommunitiesByMemberId(@PathVariable UUID memberId) {
        return ResponseEntity.ok(communityService.getCommunitiesByMemberId(memberId));
    }

   @PostMapping("/{communityId}/moderators/{userId}")
   public ResponseEntity<Community> addModerator(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.addModerator(communityId, userId));
   }

   @DeleteMapping("/{communityId}/moderators/{userId}")
   public ResponseEntity<Community> removeModerator(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.removeModerator(communityId, userId));
   }

   @PostMapping("/{communityId}/members/{userId}")
   public ResponseEntity<Community> addMember(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.addMember(communityId, userId));
   }

   @DeleteMapping("/{communityId}/members/{userId}")
   public ResponseEntity<Community> removeMember(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.removeMember(communityId, userId));
   }

   @PostMapping("/{communityId}/ban/{userId}")
   public ResponseEntity<Community> banUser(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.banUser(communityId, userId));
   }

   @DeleteMapping("/{communityId}/ban/{userId}")
   public ResponseEntity<Community> unbanUser(@PathVariable UUID communityId, @PathVariable UUID userId) {
       return ResponseEntity.ok(communityService.unbanUser(communityId, userId));
   }
}