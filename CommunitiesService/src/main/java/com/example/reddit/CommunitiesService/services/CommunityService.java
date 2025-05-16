package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.clients.ThreadClient;
import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;
import com.example.reddit.CommunitiesService.listeners.NotificationListener;
import com.example.reddit.CommunitiesService.publishers.CommunityPublisher;
import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.models.CommunityThread;
import com.example.reddit.CommunitiesService.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final ThreadClient threadClient;
    private final CommunityPublisher communityPublisher;

    @Autowired
    public CommunityService(CommunityRepository communityRepository, ThreadClient threadClient,
            CommunityPublisher communityPublisher) {
        this.communityRepository = communityRepository;
        this.threadClient = threadClient;
        this.communityPublisher = communityPublisher;
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> getCommunityById(UUID id) {
        return communityRepository.findById(id);
    }

    public Community createCommunity(String name, UUID topic, String description, UUID createdBy) {
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

    // Add thread to a community
    public Community addThread(UUID communityId, UUID threadId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        community.getThreadIds().add(threadId);
        return communityRepository.save(community);
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

    public Community addMember(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        community.getMemberIds().add(userId);
        Community saved = communityRepository.save(community);

//        // fire the event *after* save
//        events.publishEvent(new CommunityMemberAddedEvent(userId));


        CommunityMemberAddedEvent memberAdded = new CommunityMemberAddedEvent(userId);


        communityPublisher.setMember(memberAdded);

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
        community.getMemberIds().add(userId);
        return communityRepository.save(community);
    }

    // check if user is banned from community
    public boolean isUserBanned(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        return community.getBannedUserIds().contains(userId);
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

    /**
     * Fetch all threads for the given community, sort by creation date
     * descending.
     */
    public List<CommunityThread> getCommunityThreadsByDate(UUID communityId) {
        Community c = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        List<CommunityThread> threads = new ArrayList<>();
        // Fetch threads by their IDs
        for (UUID threadId : c.getThreadIds()) {
            CommunityThread thread = threadClient.getThreadById(threadId);
            // add thread to threads
            if (thread != null) {
                threads.add(thread);
                // print thread createdAt
                System.out.println("Thread ID: " + thread.getId() + ", Created At: " + thread.getCreatedAt());
            }
        }

        // sort threads by date the return them
        return threads.stream()
                .sorted(Comparator
                        .comparing(CommunityThread::getCreatedAt,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed()
                )
                .collect(Collectors.toList());

    }

    /**
     * Fetch all threads for the given community, sort by top descending.
     */
    public List<CommunityThread> getCommunityThreadsByTop(UUID communityId) {
        Community c = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        List<CommunityThread> threads = new ArrayList<>();

        for (UUID threadId : c.getThreadIds()) {
            CommunityThread thread = threadClient.getThreadById(threadId);
            // add thread to threads
            if (thread != null) {
                threads.add(thread);
                // print thread upvotes and downvotes
                System.out.println("Thread ID: " + thread.getId() + ", Upvotes: " + thread.getUpVotes() +
                        ", Downvotes: " + thread.getDownVotes());
            }
        }

        // sort threads by top which is (upvotes - downvotes) and return them
        return threads.stream()
                .sorted(Comparator
                        .comparingInt((CommunityThread t) ->
                                (t.getUpVotes()   != null ? t.getUpVotes()   : 0)
                                        - (t.getDownVotes() != null ? t.getDownVotes() : 0)
                        )
                        .reversed()
                )
                .collect(Collectors.toList());

    }

}