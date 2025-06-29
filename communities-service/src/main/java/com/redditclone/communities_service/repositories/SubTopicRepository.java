package com.redditclone.communities_service.repositories;

import com.redditclone.communities_service.models.SubTopic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubTopicRepository extends MongoRepository<SubTopic, UUID> {
//    List<SubTopic> findByTopicId(UUID topicId);
    boolean existsByName(String name);
}