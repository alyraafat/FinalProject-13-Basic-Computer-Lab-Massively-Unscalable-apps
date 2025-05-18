package com.example.reddit.ThreadsService.services;

import com.example.reddit.ThreadsService.clients.CommunityClient;
import com.example.reddit.ThreadsService.clients.UserClient;
import com.example.reddit.ThreadsService.dto.ReportRequest;
import com.example.reddit.ThreadsService.models.*;
import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.rabbitmq.ThreadProducer;
import com.example.reddit.ThreadsService.repositories.LogRepository;
import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.*;

@Service
public class ThreadService {
    @Autowired
    private final ThreadRepository threadRepository;
    private final LogRepository logRepository;
    private final UserClient userClient;
    private final ThreadProducer threadProducer;
    private final LogReflectionFactory logReflectionFactory;
    private final CommentService commentService;
    private final CommunityClient communityClient;

    @Autowired
    public ThreadService(ThreadRepository threadRepository, LogRepository logRepository, UserClient userClient, ThreadProducer threadProducer, LogReflectionFactory logReflectionFactory, CommentService commentService, CommunityClient communityClient) {
        this.threadRepository = threadRepository;
        this.logRepository=logRepository;
        this.userClient=userClient;
        this.threadProducer=threadProducer;
        this.logReflectionFactory=logReflectionFactory;
        this.commentService=commentService;
        this.communityClient=communityClient;
    }

    public List<String> testGetBlockedUsers() {
        System.out.println("testGetBlockedUsers");
        UUID uuid = UUID.randomUUID();
        List<String> blocks = userClient.getBlockList(uuid.toString());
        System.out.println("blocks has been returned: " + blocks);
        return blocks;
    }

    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    public Optional<Thread> getThreadById(UUID id) {
        return threadRepository.findById(id);
    }

    public boolean isUserBanned(UUID communityId, UUID userId) {
        return communityClient.isUserBanned(communityId, userId);
    }

    public Thread createThread(Thread thread, UUID userId) {

        // Check if the user is banned from the community
        if (isUserBanned(thread.getCommunityId(), userId)) {
            throw new RuntimeException("User is banned from this community");
        }

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
                .comments(thread.getComments())
                .build();
        Thread saved = threadRepository.save(thread);

        logReflectionFactory.createLog(ActionType.POST, thread.getAuthorId(), saved.getId());

        threadProducer.sendThreadNotificationRequest(saved.getId());

        return saved;
    }
    @CacheEvict(value = "trending_cache", key = "#communityId")
    public void deleteThread(UUID communityId,UUID id) {
        Thread thread = threadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        threadRepository.deleteById(id);
    }


    public List<Thread> getThreadsByCommunity(UUID communityId) {
        return threadRepository.findByCommunityId(communityId);
    }

    public List<Thread> getThreadsByAuthor(UUID authorId) {
        return threadRepository.findByAuthorId(authorId);
    }

    public List<Thread> getThreadsByTopic(UUID topic) {
        return threadRepository.findByTopic(topic);
    }
    @CacheEvict(value = "trending_cache", key = "#result.communityId")
    public Thread addComment(UUID threadId, Comment comment, UUID userId) {

        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));

        // Check if the user is banned from the community
        if (isUserBanned(thread.getCommunityId(), userId)) {
            throw new RuntimeException("User is banned from this community");
        }

        Comment newComment=commentService.createComment(comment);
        thread.getComments().add(newComment);
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
                .comments(thread.getComments())
                .build();

        Thread saved = threadRepository.save(thread);

        logReflectionFactory.createLog(ActionType.COMMENT, thread.getAuthorId(), saved.getId());

        threadProducer.sendThreadNotificationRequest(threadId);

        return saved;
    }

    @CacheEvict(value = "trending_cache", key = "#result.communityId")
    public Thread updateThread(UUID threadId, Thread newThread)
    {
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        thread = new Thread.Builder()
                .id(newThread.getId())
                .topic(newThread.getTopic())
                .title(newThread.getTitle())
                .content(newThread.getContent())
                .authorId(newThread.getAuthorId())
                .createdAt(newThread.getCreatedAt())
                .upVotes(newThread.getUpVotes() )
                .downVotes(newThread.getDownVotes())
                .communityId(newThread.getCommunityId())
                .comments(newThread.getComments())
                .build();
        return threadRepository.save(thread);
    }
    @CacheEvict(value = "trending_cache", key = "#result.communityId")
    public Thread removeComment(UUID threadId, UUID commentId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        // Find and remove comment with matching ID, capturing the result
        boolean commentRemoved = thread.getComments().removeIf(comment -> comment.getId().equals(commentId));

        // Throw exception if comment wasn't found
        if (!commentRemoved) {
            throw new RuntimeException("Comment not found with ID: " + commentId);
        }

        commentService.deleteComment(commentId);
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
                .comments(thread.getComments())
                .build();
        return threadRepository.save(thread);
    }

    @CacheEvict(value = "trending_cache", key = "#result.communityId")
    public Thread upvote(UUID threadId, UUID userId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));

        // Check if the user is banned from the community
//        if (isUserBanned(thread.getCommunityId(), userId)) {
//            throw new RuntimeException("User is banned from this community");
//        }

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
                .comments(thread.getComments())
                .build();
        Thread saved = threadRepository.save(thread);

        logReflectionFactory.createLog(ActionType.UPVOTE, thread.getAuthorId(), saved.getId());

        threadProducer.sendThreadNotificationRequest(threadId);

        return saved;
    }
    @CacheEvict(value = "trending_cache", key = "#result.communityId")
    public Thread downvote(UUID threadId, UUID userId) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));

        // Check if the user is banned from the community
        if (isUserBanned(thread.getCommunityId(), userId)) {
            throw new RuntimeException("User is banned from this community");
        }

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
                .comments(thread.getComments())
                .build();

        Thread saved = threadRepository.save(thread);

        logReflectionFactory.createLog(ActionType.DOWNVOTE, thread.getAuthorId(), saved.getId());

        threadProducer.sendThreadNotificationRequest(threadId);

        return saved;
    }
    @Cacheable(value = "trending_cache", key = "#communityId")
    public List<Thread> getTrendingThreads(UUID communityId) {
        List<Thread> communityThreads = threadRepository.findByCommunityId(communityId);

        return communityThreads.stream()
                .sorted((t1, t2) -> t2.getUpVotes().compareTo(t1.getUpVotes()))
                .limit(5)
                .collect(Collectors.toList());
    }



    public List<Thread> recommendThreadsByUpvotes(UUID userId)
    {

        // TODO: PLACE CALL TO ALIS FUNCVTION TO GET LOGS FOR THIS USER AND THIS ACTION TYPE

         List <Log>  userUpVoteLogs = logRepository.findByUserIdAndActionType(userId,ActionType.UPVOTE);

        Set<UUID> threadIds= new HashSet<>();


         for (Log log : userUpVoteLogs)
        {
            threadIds.add(log.getThreadId());
        }


        List<Thread> recommendedThreads= new ArrayList<>();

        Set<UUID> topicIds= new HashSet<>();
        for(UUID threadId : threadIds)
        {
            Optional<Thread> thread= threadRepository.findById(threadId);
            if(!thread.isEmpty())
            {
                topicIds.add(thread.get().getTopic());
            }

        }


        for(UUID topicId: topicIds )
        {
            List<Thread> threads2= this.getThreadsByTopic(topicId);
            recommendedThreads.addAll(threads2);
        }


        return recommendedThreads;


    }





    public void createReport (  UUID userReporting, UUID itemReported,String reportDescription,UUID communityId )
    {

        threadProducer.sendReportRequest(userReporting,itemReported,reportDescription,communityId);
    }
    }