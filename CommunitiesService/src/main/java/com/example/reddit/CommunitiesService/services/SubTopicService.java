package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.models.SubTopic;
import com.example.reddit.CommunitiesService.models.Topic;
import com.example.reddit.CommunitiesService.repositories.SubTopicRepository;
import com.example.reddit.CommunitiesService.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubTopicService {
    private final SubTopicRepository subTopicRepository;
    private final TopicService topicService;
    private final TopicRepository topicRepository;

    @Autowired
    public SubTopicService(SubTopicRepository subTopicRepository, TopicService topicService,
            TopicRepository topicRepository) {
        this.subTopicRepository = subTopicRepository;
        this.topicService = topicService;
        this.topicRepository = topicRepository;
    }

    public List<SubTopic> getAllSubTopics() {
        return subTopicRepository.findAll();
    }

    public Optional<SubTopic> getSubTopicById(UUID id) {
        return subTopicRepository.findById(id);
    }

    public List<SubTopic> getSubTopicsByTopic(UUID topicId) {
        return subTopicRepository.findByTopicId(topicId);
    }

    public SubTopic createSubTopic(SubTopic subTopic) {
        if (subTopicRepository.existsByNameAndTopicId(subTopic.getName(), subTopic.getTopicId())) {
            throw new RuntimeException("SubTopic name already exists in this topic");
        }
        SubTopic savedSubTopic = subTopicRepository.save(subTopic);
        topicService.addSubtopic(subTopic.getTopicId(), savedSubTopic.getId());
        return savedSubTopic;
    }

    public SubTopic addSubTopic(String name, UUID topicId) {
        // Verify that the topic exists
        if (!topicRepository.existsById(topicId)) {
            throw new RuntimeException("Topic not found");
        }

        // Create the subtopic using the builder
        SubTopic subTopic = SubTopic.builder()
                .id(UUID.randomUUID())
                .name(name)
                .topicId(topicId)
                .build();

        SubTopic savedSubTopic = subTopicRepository.save(subTopic);

        // Add this subtopic to the parent topic's subtopics list
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        topic.getSubtopicIds().add(savedSubTopic.getId());
        topicRepository.save(topic);

        return savedSubTopic;
    }

    public SubTopic updateSubTopic(UUID id, String name) {
        // Retrieve the existing subtopic
        SubTopic existingSubTopic = subTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));

        // Check if name already exists (if name is being updated)
        if (name != null && !name.equals(existingSubTopic.getName()) &&
                subTopicRepository.existsByName(name)) {
            throw new RuntimeException("SubTopic name already exists");
        }

        SubTopic updated = SubTopic.builder()
                .id(existingSubTopic.getId())
                .name(name)
                .topicId(existingSubTopic.getTopicId())
                .build();
        return subTopicRepository.save(updated);
    }

    public void deleteSubTopic(UUID id) {
        SubTopic subTopic = subTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));
        topicService.removeSubtopic(subTopic.getTopicId(), id);
        subTopicRepository.deleteById(id);
    }

}