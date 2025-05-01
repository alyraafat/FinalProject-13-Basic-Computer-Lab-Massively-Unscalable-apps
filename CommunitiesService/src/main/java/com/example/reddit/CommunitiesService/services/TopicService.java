package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.models.Topic;
import com.example.reddit.CommunitiesService.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(String id) {
        return topicRepository.findById(id);
    }

    public Topic createTopic(Topic topic) {
        if (topicRepository.existsByName(topic.getName())) {
            throw new RuntimeException("Topic name already exists");
        }
        return topicRepository.save(topic);
    }

    public void deleteTopic(String id) {
        topicRepository.deleteById(id);
    }

//    public Topic addCommunity(UUID topicId, UUID communityId) {
//        Topic topic = topicRepository.findById(topicId)
//            .orElseThrow(() -> new RuntimeException("Topic not found"));
//        topic.getCommunityIds().add(communityId);
//        return topicRepository.save(topic);
//    }
//
//    public Topic removeCommunity(UUID topicId, UUID communityId) {
//        Topic topic = topicRepository.findById(topicId)
//            .orElseThrow(() -> new RuntimeException("Topic not found"));
//        topic.getCommunityIds().remove(communityId);
//        return topicRepository.save(topic);
//    }
//
//    public Topic addSubtopic(UUID topicId, UUID subtopicId) {
//        Topic topic = topicRepository.findById(topicId)
//            .orElseThrow(() -> new RuntimeException("Topic not found"));
//        topic.getSubtopicIds().add(subtopicId);
//        return topicRepository.save(topic);
//    }
//
//    public Topic removeSubtopic(UUID topicId, UUID subtopicId) {
//        Topic topic = topicRepository.findById(topicId)
//            .orElseThrow(() -> new RuntimeException("Topic not found"));
//        topic.getSubtopicIds().remove(subtopicId);
//        return topicRepository.save(topic);
//    }

    public Topic getTopicByName(String name) {
        return topicRepository.findByName(name);
    }

    public Topic getTopicByCommunity(String communityId) {
        return topicRepository.findByCommunities_Id(communityId);
    }
}