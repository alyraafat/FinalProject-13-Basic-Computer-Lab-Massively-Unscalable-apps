package com.example.reddit.CommunitiesService.repositories;

import com.example.reddit.CommunitiesService.models.Community;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommunityRepository extends MongoRepository<Community, String> {
//    List<Community> findByTopicId(UUID topicId);
//    List<Community> findByModeratorIdsContaining(UUID moderatorId);
//    List<Community> findByMemberIdsContaining(UUID memberId);
    boolean existsByName(String name);


    // “topic” is a DBRef → look inside its “id” property
    List<Community> findByTopic_Id(String topicId);

    // “moderators” is a List<User> → look inside each User’s “id”
    List<Community> findByModerators_Id(String moderatorId);

    // “members” is a List<User> → look inside each User’s “id”
    List<Community> findByMembers_Id(String memberId);
}