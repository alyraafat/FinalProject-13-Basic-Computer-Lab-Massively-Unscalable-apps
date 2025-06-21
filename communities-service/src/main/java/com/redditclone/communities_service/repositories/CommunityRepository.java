package com.redditclone.communities_service.repositories;

import com.redditclone.communities_service.models.Community;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommunityRepository extends MongoRepository<Community, UUID> {

    List<Community> findByModeratorIdsContaining(UUID moderatorId);
    List<Community> findByMemberIdsContaining(UUID memberId);
    boolean existsByName(String name);
    List<Community> findByTopicId(UUID topicId);

}