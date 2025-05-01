package com.example.reddit.CommunitiesService.repositories;

import com.example.reddit.CommunitiesService.models.SubTopic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubTopicRepository extends MongoRepository<SubTopic, String> {
    List<SubTopic> findByTopicId(String topicId);
    boolean existsByNameAndTopicId(String name, String topicId);
}