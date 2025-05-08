package com.example.reddit.ThreadsService.controllers;

import com.example.reddit.ThreadsService.models.Comment;
import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {
    private final ThreadService threadService;

    @Autowired
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping("test")
    public ResponseEntity<List<String>> test(){
        List<String> blocks = threadService.testGetBlockedUsers();
        return ResponseEntity.ok(blocks);
    }

    @GetMapping
    public ResponseEntity<List<Thread>> getAllThreads() {
        return ResponseEntity.ok(threadService.getAllThreads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Thread> getThreadById(@PathVariable UUID id) {
        return threadService.getThreadById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Thread> createThread(@RequestBody Thread thread) {
        return ResponseEntity.ok(threadService.createThread(thread));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThread(@PathVariable UUID id) {
        threadService.deleteThread(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<List<Thread>> getThreadsByCommunity(@PathVariable UUID communityId) {
        return ResponseEntity.ok(threadService.getThreadsByCommunity(communityId));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Thread>> getThreadsByAuthor(@PathVariable UUID authorId) {
        return ResponseEntity.ok(threadService.getThreadsByAuthor(authorId));
    }

    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<Thread>> getThreadsByTopic(@PathVariable UUID topic) {
        return ResponseEntity.ok(threadService.getThreadsByTopic(topic));
    }

    @PostMapping("/{threadId}/comments/{commentId}")
    public ResponseEntity<Thread> addComment(@PathVariable UUID threadId, @RequestBody Comment comment) {
        return ResponseEntity.ok(threadService.addComment(threadId, comment));
    }

    @DeleteMapping("/{threadId}/comments/{commentId}")
    public ResponseEntity<Thread> removeComment(@PathVariable UUID threadId, @PathVariable UUID commentId) {
        return ResponseEntity.ok(threadService.removeComment(threadId, commentId));
    }

    @PostMapping("/{threadId}/upvote")
    public ResponseEntity<Thread> upvoteThread(@PathVariable UUID threadId) {
        return ResponseEntity.ok(threadService.upvote(threadId));
    }

    @PostMapping("/{threadId}/downvote")
    public ResponseEntity<Thread> downvoteThread(@PathVariable UUID threadId) {
        return ResponseEntity.ok(threadService.downvote(threadId));
    }

//    @PostMapping("/recommendThreads/{userId}")
//    public ResponseEntity<List<Thread>> recommendThreadsByUpvote(@PathVariable  UUID userId)
//    {
//        return ResponseEntity.ok(threadService.recommendThreadsByUpvotes(userId));
//    }


}