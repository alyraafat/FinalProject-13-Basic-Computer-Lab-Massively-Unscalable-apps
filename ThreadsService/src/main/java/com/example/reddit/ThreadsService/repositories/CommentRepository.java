package com.example.reddit.ThreadsService.repositories;

import com.example.reddit.ThreadsService.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends MongoRepository<Comment, UUID> {
    List<Comment> findByThreadId(UUID threadId);
}