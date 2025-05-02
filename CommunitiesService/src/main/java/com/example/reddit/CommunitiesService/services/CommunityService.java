package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> getCommunityById(UUID id) {
        return communityRepository.findById(id);
    }

    public Community createCommunity(Community community) {
        if (communityRepository.existsByName(community.getName())) {
            throw new RuntimeException("Community name already exists");
        }
        return communityRepository.save(community);
    }

    public Community addCommunity(String name, UUID topic, String description, UUID createdBy) {
        if (communityRepository.existsByName(name)) {
            throw new RuntimeException("Community name already exists");
        }

        Community community = Community.builder()
                .id(UUID.randomUUID())
                .name(name)
                .topic(topic)
                .description(description)
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .build();

        return communityRepository.save(community);
    }

    public Community updateCommunity(UUID id, String name, String description, UUID topic) {
        // Retrieve the existing community
        Community existingCommunity = communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Check if name already exists (if name is being updated)
        if (name != null && !name.equals(existingCommunity.getName()) &&
                communityRepository.existsByName(name)) {
            throw new RuntimeException("Community name already exists");
        }

        // Using the builder to create an updated version of the community
        Community updatedCommunity = Community.builder()
                .id(existingCommunity.getId())
                .name(name != null ? name : existingCommunity.getName())
                .description(description != null ? description : existingCommunity.getDescription())
                .topic(topic != null ? topic : existingCommunity.getTopic())
                .createdAt(existingCommunity.getCreatedAt())  // Preserve creation timestamp
                .createdBy(existingCommunity.getCreatedBy())  // Preserve creator
                .moderators(existingCommunity.getModerators())
                .members(existingCommunity.getMembers())
                .bannedUsers(existingCommunity.getBannedUsers())
                .threads(existingCommunity.getThreads())
                .build();

        // Save and return the updated community
        return communityRepository.save(updatedCommunity);
    }


    public void deleteCommunity(UUID id) {
        communityRepository.deleteById(id);
    }


    public List<Community> getCommunitiesByTopicId(UUID topicId) {
        return communityRepository.findByTopicId(topicId);
    }

    public List<Community> getCommunitiesByModeratorId(UUID moderatorId) {
        return communityRepository.findByModeratorIdsContaining(moderatorId);
    }

    public List<Community> getCommunitiesByMemberId(UUID memberId) {
        return communityRepository.findByMemberIdsContaining(memberId);
    }




   public List<Community> getCommunitiesByModerator(UUID moderatorId) {
       return communityRepository.findByModeratorIdsContaining(moderatorId);
   }

   public List<Community> getCommunitiesByMember(UUID memberId) {
       return communityRepository.findByMemberIdsContaining(memberId);
   }

   public Community addModerator(UUID communityId, UUID userId) {
       Community community = communityRepository.findById(communityId)
           .orElseThrow(() -> new RuntimeException("Community not found"));
       community.getModerators().add(userId);
       return communityRepository.save(community);
   }

   public Community removeModerator(UUID communityId, UUID userId) {
       Community community = communityRepository.findById(communityId)
           .orElseThrow(() -> new RuntimeException("Community not found"));
       community.getModerators().remove(userId);
       return communityRepository.save(community);
   }

   public Community addMember(UUID communityId, UUID userId) {
       Community community = communityRepository.findById(communityId)
           .orElseThrow(() -> new RuntimeException("Community not found"));
       community.getMembers().add(userId);
       return communityRepository.save(community);
   }

   public Community removeMember(UUID communityId, UUID userId) {
       Community community = communityRepository.findById(communityId)
           .orElseThrow(() -> new RuntimeException("Community not found"));
       community.getMembers().remove(userId);
       return communityRepository.save(community);
   }

   public Community banUser(UUID communityId, UUID userId) {
       Community community = communityRepository.findById(communityId)
           .orElseThrow(() -> new RuntimeException("Community not found"));
       community.getBannedUsers().add(userId);
       community.getMembers().remove(userId);
       return communityRepository.save(community);
   }

   public Community unbanUser(UUID communityId, UUID userId) {
       Community community = communityRepository.findById(communityId)
           .orElseThrow(() -> new RuntimeException("Community not found"));
       community.getBannedUsers().remove(userId);
       return communityRepository.save(community);
   }
}