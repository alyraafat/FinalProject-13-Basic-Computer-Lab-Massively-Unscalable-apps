package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Community> getCommunityById(String id) {
        return communityRepository.findById(id);
    }

    public Community createCommunity(Community community) {
        if (communityRepository.existsByName(community.getName())) {
            throw new RuntimeException("Community name already exists");
        }
        return communityRepository.save(community);
    }

    public void deleteCommunity(String id) {
        communityRepository.deleteById(id);
    }


    public List<Community> getCommunitiesByTopicId(String topicId) {
        return communityRepository.findByTopic_Id(topicId);
    }

    public List<Community> getCommunitiesByModeratorId(String moderatorId) {
        return communityRepository.findByModerators_Id(moderatorId);
    }

    public List<Community> getCommunitiesByMemberId(String memberId) {
        return communityRepository.findByMembers_Id(memberId);
    }




//    public List<Community> getCommunitiesByModerator(UUID moderatorId) {
//        return communityRepository.findByModeratorIdsContaining(moderatorId);
//    }
//
//    public List<Community> getCommunitiesByMember(UUID memberId) {
//        return communityRepository.findByMemberIdsContaining(memberId);
//    }
//
//    public Community addModerator(UUID communityId, UUID userId) {
//        Community community = communityRepository.findById(communityId)
//            .orElseThrow(() -> new RuntimeException("Community not found"));
//        community.getModeratorIds().add(userId);
//        return communityRepository.save(community);
//    }
//
//    public Community removeModerator(UUID communityId, UUID userId) {
//        Community community = communityRepository.findById(communityId)
//            .orElseThrow(() -> new RuntimeException("Community not found"));
//        community.getModeratorIds().remove(userId);
//        return communityRepository.save(community);
//    }
//
//    public Community addMember(UUID communityId, UUID userId) {
//        Community community = communityRepository.findById(communityId)
//            .orElseThrow(() -> new RuntimeException("Community not found"));
//        community.getMemberIds().add(userId);
//        return communityRepository.save(community);
//    }
//
//    public Community removeMember(UUID communityId, UUID userId) {
//        Community community = communityRepository.findById(communityId)
//            .orElseThrow(() -> new RuntimeException("Community not found"));
//        community.getMemberIds().remove(userId);
//        return communityRepository.save(community);
//    }
//
//    public Community banUser(UUID communityId, UUID userId) {
//        Community community = communityRepository.findById(communityId)
//            .orElseThrow(() -> new RuntimeException("Community not found"));
//        community.getBannedUserIds().add(userId);
//        community.getMemberIds().remove(userId);
//        return communityRepository.save(community);
//    }
//
//    public Community unbanUser(UUID communityId, UUID userId) {
//        Community community = communityRepository.findById(communityId)
//            .orElseThrow(() -> new RuntimeException("Community not found"));
//        community.getBannedUserIds().remove(userId);
//        return communityRepository.save(community);
//    }
}