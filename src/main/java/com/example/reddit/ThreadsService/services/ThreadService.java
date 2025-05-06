package com.example.reddit.ThreadsService.services;

import com.example.reddit.ThreadsService.models.ActionType;
import com.example.reddit.ThreadsService.models.Comment;
import com.example.reddit.ThreadsService.models.Log;
import com.example.reddit.ThreadsService.models.Thread;
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

    @Autowired
    public ThreadService(ThreadRepository threadRepository, LogRepository logRepository) {
        this.threadRepository = threadRepository;
        this.logRepository=logRepository;
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
    @CacheEvict(value = "trending_cache", key = "#thread.communityId")
    public void deleteThread(UUID id) {
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
    @CacheEvict(value = "trending_cache", key = "#thread.communityId")
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
    @CacheEvict(value = "trending_cache", key = "#thread.communityId")
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

    @CacheEvict(value = "trending_cache", key = "#thread.communityId")
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
    @CacheEvict(value = "trending_cache", key = "#thread.communityId")
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


        // If topic is changed from string to uuid then I need to call the communities microservice through the message queue to get the entire subtopic then I loop on the
        //returned list of subtopics and get their topics through synchronous communication
        // List<Subtopic> subTopicsOfInterest = communities service call

        //loop on threads and see which ones have same topic ids like the ids

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
            recommendedThreads.add((Thread) threads2);
        }






        return recommendedThreads;


    }



    }