package com.example.reddit.ThreadsService.services;

import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Thread addComment(UUID threadId, UUID commentId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.getCommentIds().add(commentId);
        return threadRepository.save(thread);
    }

    public Thread removeComment(UUID threadId, UUID commentId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.getCommentIds().remove(commentId);
        return threadRepository.save(thread);
    }

    public Thread upvote(UUID threadId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.setUpVotes(thread.getUpVotes() + 1);
        return threadRepository.save(thread);
    }

    public Thread downvote(UUID threadId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.setDownVotes(thread.getDownVotes() + 1);
        return threadRepository.save(thread);
    }
}