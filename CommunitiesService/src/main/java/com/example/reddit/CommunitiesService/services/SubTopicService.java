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

    @Autowired
    public SubTopicService(SubTopicRepository subTopicRepository) {
        this.subTopicRepository = subTopicRepository;
    }

    public List<SubTopic> getAllSubTopics() {
        return subTopicRepository.findAll();
    }

    public Optional<SubTopic> getSubTopicById(UUID id) {
        return subTopicRepository.findById(id);
    }

//    public List<SubTopic> getSubTopicsByTopic(UUID topicId) {
//        return subTopicRepository.findByTopicId(topicId);
//    }

//    public SubTopic createSubTopic(SubTopic subTopic) {
//        if (subTopicRepository.existsByNameAndTopicId(subTopic.getName(), subTopic.getTopicId())) {
//            throw new RuntimeException("SubTopic name already exists in this topic");
//        }
//        SubTopic savedSubTopic = subTopicRepository.save(subTopic);
//        topicService.addSubtopic(subTopic.getTopicId(), savedSubTopic.getId());
//        return savedSubTopic;
//    }

    public SubTopic addSubTopic(String name) {
        // Create the subtopic using the builder
        SubTopic subTopic = SubTopic.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();

        SubTopic savedSubTopic = subTopicRepository.save(subTopic);

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
                .build();
        return subTopicRepository.save(updated);
    }

    public void deleteSubTopic(UUID id) {
        SubTopic subTopic = subTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));
        subTopicRepository.deleteById(id);
    }

}