package com.example.reddit.CommunitiesService.repositories;

import com.example.reddit.CommunitiesService.models.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
    boolean existsByName(String name);
    Topic findByName(String name);
    Topic findByCommunities_Id(String communityId);
}