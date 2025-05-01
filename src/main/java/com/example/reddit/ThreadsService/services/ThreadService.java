package com.example.reddit.ThreadsService.services;

import com.example.reddit.ThreadsService.models.ActionType;
import com.example.reddit.ThreadsService.models.Comment;
import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ThreadService {
    private final ThreadRepository threadRepository;

    @Autowired
    public ThreadService(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    public Optional<Thread> getThreadById(UUID id) {
        return threadRepository.findById(id);
    }

    public Thread createThread(Thread thread) {
        return threadRepository.save(thread);
    }
    @CacheEvict(value = "thread", allEntries = true)
    public void deleteThread(UUID id) {
        threadRepository.deleteById(id);
    }

    public List<Thread> getThreadsByCommunity(UUID communityId) {
        return threadRepository.findByCommunityId(communityId);
    }

    public List<Thread> getThreadsByAuthor(UUID authorId) {
        return threadRepository.findByAuthorId(authorId);
    }

    public List<Thread> getThreadsByTopic(String topic) {
        return threadRepository.findByTopic(topic);
    }
    @CachePut(value = "thread", key = "#threadId")
    public Thread addComment(UUID threadId, Comment comment) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.getCommentIds().add(comment);
        thread = new Thread.Builder()
                .id(thread.getId())
                .topic(thread.getTopic())
                .title(thread.getTitle())
                .content(thread.getContent())
                .authorId(thread.getAuthorId())
                .createdAt(thread.getCreatedAt())
                .upVotes(thread.getUpVotes() )
                .downVotes(thread.getDownVotes())
                .communityId(thread.getCommunityId())
                .comments(thread.getCommentIds())
                .build();
        return threadRepository.save(thread);
    }
    @CachePut(value = "thread", key = "#threadId")
    public Thread removeComment(UUID threadId, UUID commentId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.getCommentIds().remove(commentId);
        thread = new Thread.Builder()
                .id(thread.getId())
                .topic(thread.getTopic())
                .title(thread.getTitle())
                .content(thread.getContent())
                .authorId(thread.getAuthorId())
                .createdAt(thread.getCreatedAt())
                .upVotes(thread.getUpVotes() )
                .downVotes(thread.getDownVotes())
                .communityId(thread.getCommunityId())
                .comments(thread.getCommentIds())
                .build();
        return threadRepository.save(thread);
    }
    @CachePut(value = "thread", key = "#threadId")
    public Thread upvote(UUID threadId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread = new Thread.Builder()
                .id(thread.getId())
                .topic(thread.getTopic())
                .title(thread.getTitle())
                .content(thread.getContent())
                .authorId(thread.getAuthorId())
                .createdAt(thread.getCreatedAt())
                .upVotes(thread.getUpVotes() + 1)
                .downVotes(thread.getDownVotes())
                .communityId(thread.getCommunityId())
                .comments(thread.getCommentIds())
                .build();
        return threadRepository.save(thread);
    }

    public Thread downvote(UUID threadId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread = new Thread.Builder()
                .id(thread.getId())
                .topic(thread.getTopic())
                .title(thread.getTitle())
                .content(thread.getContent())
                .authorId(thread.getAuthorId())
                .createdAt(thread.getCreatedAt())
                .upVotes(thread.getUpVotes() )
                .downVotes(thread.getDownVotes() + 1 )
                .communityId(thread.getCommunityId())
                .comments(thread.getCommentIds())
                .build();
        return threadRepository.save(thread);
    }
    @Cacheable(value = "thread", key = "'trending'")
    public List<Thread> getTrendingThreads (){
        return threadRepository.findTop3ByOrderByUpVotesDesc();
    }



}