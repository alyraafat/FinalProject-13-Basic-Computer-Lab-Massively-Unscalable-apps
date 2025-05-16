package com.example.reddit.ThreadsService.controllers;

import com.example.reddit.ThreadsService.dto.ReportRequest;
import com.example.reddit.ThreadsService.models.Comment;
import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.models.ThreadDto;
import com.example.reddit.ThreadsService.models.ThreadMapper;
import com.example.reddit.ThreadsService.services.ThreadService;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/threads")
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


    @GetMapping("/allThreads")
    public ResponseEntity<List<Thread>> getAllThreads() {
        List<Thread> threads = threadService.getAllThreads();
        return ResponseEntity.ok(threads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Thread> getThreadById(@PathVariable UUID id) {
        return threadService.getThreadById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createThread(
            @RequestBody Thread thread,
            @RequestHeader("X-User-Id") UUID userId) {
        try {
            Thread created = threadService.createThread(thread, userId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);
        } catch (RuntimeException ex) {
            if ("User is banned from this community".equals(ex.getMessage())) {
                Map<String, String> error = Collections.singletonMap(
                        "message", ex.getMessage()
                );
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(error);
            }
            throw ex;
        }
    }

    @DeleteMapping("/communities/{communityId}/threads/{id}")
    public ResponseEntity<Void> deleteThread(@PathVariable UUID communityId,@PathVariable UUID id) {
        threadService.deleteThread(communityId,id);
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

    @PostMapping("/{threadId}/comments/addComment")
    public ResponseEntity<?> addComment(
            @PathVariable UUID threadId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody Comment comment
    ) {
        try {
            Thread updated = threadService.addComment(threadId, comment, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            if ("User is banned from this community".equals(ex.getMessage())) {
                Map<String, String> error = Collections.singletonMap(
                        "message", ex.getMessage()
                );
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(error);
            }
            throw ex;
        }
    }

    @DeleteMapping("/{threadId}/comments/{commentId}")
    public ResponseEntity<Thread> removeComment(@PathVariable UUID threadId, @PathVariable UUID commentId) {
        return ResponseEntity.ok(threadService.removeComment(threadId, commentId));
    }

    @PostMapping("/{threadId}/upvote")
    public ResponseEntity<?> upvoteThread(
            @PathVariable UUID threadId,
            @RequestHeader("X-User-Id") UUID userId
    ) {
        try {
            Thread updated = threadService.upvote(threadId, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            if ("User is banned from this community".equals(ex.getMessage())) {
                Map<String, String> error = Collections.singletonMap(
                        "message", ex.getMessage()
                );
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(error);
            }
            throw ex;
        }
    }

    @PostMapping("/{threadId}/downvote/{userId}")
    public ResponseEntity<?> downvoteThread(
            @PathVariable UUID threadId,
            @RequestHeader("X-User-Id") UUID userId
    ) {
        try {
            Thread updated = threadService.downvote(threadId, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            if ("User is banned from this community".equals(ex.getMessage())) {
                Map<String, String> error = Collections.singletonMap(
                        "message", ex.getMessage()
                );
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(error);
            }
            throw ex;
        }
    }

    @PutMapping("/{id}")
    public Thread updateThread(@PathVariable UUID id , @RequestBody Thread newThread)
    {
      return threadService.updateThread(id,newThread);
    }

    @PostMapping("/recommendThreads")
    public ResponseEntity<List<Thread>> recommendThreadsByUpvote(@RequestHeader("X-User-Id") UUID userId)
    {
        return ResponseEntity.ok(threadService.recommendThreadsByUpvotes(userId));
    }

    @GetMapping("/community/{communityId}/trending")
    public ResponseEntity<List<Thread>> getTrendingThreads(@PathVariable UUID communityId) {
        List<Thread> trendingThreads = threadService.getTrendingThreads(communityId);
        return ResponseEntity.ok(trendingThreads);
    }

    @PostMapping("/createReportRequest")
    public void createReportRequest(@RequestBody ReportRequest request)
    {
        threadService.createReport(request.getUserReporting(),request.getItemReported(), request.getReportDescription(), request.getCommunityId());
    }


}