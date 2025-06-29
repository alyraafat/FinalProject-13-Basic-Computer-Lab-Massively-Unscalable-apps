package com.redditclone.thread_service.repositories;

import com.redditclone.thread_service.models.Thread;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, UUID> {
    List<Thread> findByCommunityId(UUID communityId);
    List<Thread> findByAuthorId(UUID authorId);
    List<Thread> findByTopic(UUID topic);
}