package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.models.SubTopic;
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

    public Optional<Topic> getTopicById(UUID id) {
        return topicRepository.findById(id);
    }

    public Topic createTopic(Topic topic) {
        if (topicRepository.existsByName(topic.getName())) {
            throw new RuntimeException("Topic name already exists");
        }
        return topicRepository.save(topic);
    }

    public Topic addTopic(String name) {
        Topic topic = Topic.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();

        return topicRepository.save(topic);
    }

    public Topic updateTopic(UUID id, String name) {
        // Retrieve the existing topic
        Topic existingTopic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        // Check if name already exists (if name is being updated)
        if (name != null && !name.equals(existingTopic.getName()) &&
                topicRepository.existsByName(name)) {
            throw new RuntimeException("Topic name already exists");
        }

        // Using the builder to create an updated version of the topic
        Topic updatedTopic = Topic.builder()
                .id(existingTopic.getId())
                .name(name != null ? name : existingTopic.getName())
                .subtopics(existingTopic.getSubtopics())
                .communities(existingTopic.getCommunities())
                .build();

        // Save and return the updated topic
        return topicRepository.save(updatedTopic);
    }

    // Creating a topic with subtopics
    public Topic createTopicWithSubtopics(String name, List<UUID> subtopicIds) {
        Topic topic = Topic.builder()
                .id(UUID.randomUUID())
                .name(name)
                .subtopics(subtopicIds)
                .build();

        return topicRepository.save(topic);
    }

    public void deleteTopic(UUID id) {
        topicRepository.deleteById(id);
    }

   public Topic addCommunity(UUID topicId, UUID communityId) {
       Topic topic = topicRepository.findById(topicId)
           .orElseThrow(() -> new RuntimeException("Topic not found"));
       topic.getCommunities().add(communityId);
       return topicRepository.save(topic);
   }

   public Topic removeCommunity(UUID topicId, UUID communityId) {
       Topic topic = topicRepository.findById(topicId)
           .orElseThrow(() -> new RuntimeException("Topic not found"));
       topic.getCommunities().remove(communityId);
       return topicRepository.save(topic);
   }

   public Topic addSubtopic(UUID topicId, UUID subtopicId) {
       Topic topic = topicRepository.findById(topicId)
           .orElseThrow(() -> new RuntimeException("Topic not found"));
       topic.getSubtopics().add(subtopicId);
       return topicRepository.save(topic);
   }

   public Topic removeSubtopic(UUID topicId, UUID subtopicId) {
       Topic topic = topicRepository.findById(topicId)
           .orElseThrow(() -> new RuntimeException("Topic not found"));
       topic.getSubtopics().remove(subtopicId);
       return topicRepository.save(topic);
   }

    public Topic getTopicByName(String name) {
        return topicRepository.findByName(name);
    }

    public Topic getTopicByCommunity(UUID communityId) {
        return topicRepository.findByCommunityIdsContaining(communityId);
    }
}