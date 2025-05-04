package com.example.reddit.ThreadsService.repositories;

import com.example.reddit.ThreadsService.models.Thread;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, UUID> {
    List<Thread> findByCommunityId(UUID communityId);
    List<Thread> findByAuthorId(UUID authorId);
    List<Thread> findByTopic(UUID topic);
}