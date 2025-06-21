package com.redditclone.communities_service.repositories;

import com.redditclone.communities_service.models.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopicRepository extends MongoRepository<Topic, UUID> {
    boolean existsByName(String name);
    Topic findByName(String name);
//    Topic findByCommunityIdsContaining(UUID communityId);
}