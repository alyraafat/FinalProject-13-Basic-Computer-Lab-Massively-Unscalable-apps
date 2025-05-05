package com.example.reddit.CommunitiesService.services;

//import com.example.reddit.CommunitiesService.clients.ThreadClient;
import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;
import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    // private final ThreadClient threadClient;
    private final ApplicationEventPublisher events;

    // @Autowired
    // public CommunityService(CommunityRepository communityRepository, ThreadClient
    // threadClient, ApplicationEventPublisher events) {
    // this.communityRepository = communityRepository;
    // this.threadClient = threadClient;
    // this.events = events;
    // }

    @Autowired
    public CommunityService(CommunityRepository communityRepository, ApplicationEventPublisher events) {
        this.communityRepository = communityRepository;
        this.events = events;
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
                .topicId(topic)
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
                .topicId(topic != null ? topic : existingCommunity.getTopicId())
                .createdAt(existingCommunity.getCreatedAt()) // Preserve creation timestamp
                .createdBy(existingCommunity.getCreatedBy()) // Preserve creator
                .moderatorIds(existingCommunity.getModeratorIds())
                .memberIds(existingCommunity.getMemberIds())
                .bannedUserIds(existingCommunity.getBannedUserIds())
                .threadIds(existingCommunity.getThreadIds())
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
        community.getModeratorIds().add(userId);
        return communityRepository.save(community);
    }

    public Community removeModerator(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        community.getModeratorIds().remove(userId);
        return communityRepository.save(community);
    }

    // public Community addMember(UUID communityId, UUID userId) {
    // Community community = communityRepository.findById(communityId)
    // .orElseThrow(() -> new RuntimeException("Community not found"));
    // community.getMembers().add(userId);
    // return communityRepository.save(community);
    // }

    public Community addMember(UUID communityId, UUID userId, String username) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        community.getMemberIds().add(userId);
        Community saved = communityRepository.save(community);

        String message = String.format("User %s has joined the community %s", username, community.getName());

        String type = "COMMUNITY";

        // fire the event *after* save
        events.publishEvent(new CommunityMemberAddedEvent(type, communityId, message));

        return saved;
    }

    public Community removeMember(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        community.getMemberIds().remove(userId);
        return communityRepository.save(community);
    }

    public Community banUser(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        community.getBannedUserIds().add(userId);
        community.getMemberIds().remove(userId);
        return communityRepository.save(community);
    }

    public Community unbanUser(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        community.getBannedUserIds().remove(userId);
        return communityRepository.save(community);
    }

    /**
     * Return all communities, sorted descending by the number of members.
     */
    public List<Community> getCommunitiesByMemberCountDesc() {
        return communityRepository.findAll().stream()
                .sorted(Comparator.comparingInt((Community c) -> c.getMemberIds() != null ? c.getMemberIds().size() : 0)
                        .reversed())
                .collect(Collectors.toList());
    }

    // /**
    // * Fetch all threads for the given community, sort by creation date
    // descending.
    // */
    // public List<ThreadModel> getCommunityThreadsByDate(UUID communityId) {
    // Community c = communityRepository.findById(communityId)
    // .orElseThrow(() -> new RuntimeException("Community not found"));
    //
    // return c.getThreads().stream()
    // .map(threadClient::getById) // Optional<ThreadDto>
    // .flatMap(Optional::stream) // drop missing
    // .sorted(Comparator.comparing(ThreadModel::getCreatedAt)
    // .reversed())
    // .collect(Collectors.toList());
    // }
    //
    // /**
    // * Fetch all threads for the given community, sort by upvotes descending.
    // */
    // public List<ThreadModel> getCommunityThreadsByTop(UUID communityId) {
    // Community c = communityRepository.findById(communityId)
    // .orElseThrow(() -> new RuntimeException("Community not found"));
    //
    // return c.getThreads().stream()
    // .map(threadClient::getById) // Optional<ThreadDto>
    // .flatMap(Optional::stream) // drop missing
    // .sorted(Comparator.comparing(ThreadModel::getUpvotes)
    // .reversed())
    // .collect(Collectors.toList());
    // }

}