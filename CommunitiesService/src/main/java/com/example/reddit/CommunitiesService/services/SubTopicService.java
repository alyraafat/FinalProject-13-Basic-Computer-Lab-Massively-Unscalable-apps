package com.example.reddit.CommunitiesService.services;

import com.example.reddit.CommunitiesService.models.SubTopic;
import com.example.reddit.CommunitiesService.repositories.SubTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubTopicService {
    private final SubTopicRepository subTopicRepository;
    private final TopicService topicService;

    @Autowired
    public SubTopicService(SubTopicRepository subTopicRepository, TopicService topicService) {
        this.subTopicRepository = subTopicRepository;
        this.topicService = topicService;
    }

    public List<SubTopic> getAllSubTopics() {
        return subTopicRepository.findAll();
    }

    public Optional<SubTopic> getSubTopicById(String id) {
        return subTopicRepository.findById(id);
    }

    public List<SubTopic> getSubTopicsByTopic(String topicId) {
        return subTopicRepository.findByTopicId(topicId);
    }

//    public SubTopic createSubTopic(SubTopic subTopic) {
//        if (subTopicRepository.existsByNameAndTopicId(subTopic.getName(), subTopic.getTopicId())) {
//            throw new RuntimeException("SubTopic name already exists in this topic");
//        }
//        SubTopic savedSubTopic = subTopicRepository.save(subTopic);
//        topicService.addSubtopic(subTopic.getTopicId(), savedSubTopic.getId());
//        return savedSubTopic;
//    }
//
//    public void deleteSubTopic(UUID id) {
//        SubTopic subTopic = subTopicRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("SubTopic not found"));
//        topicService.removeSubtopic(subTopic.getTopicId(), id);
//        subTopicRepository.deleteById(id);
//    }
//
//    public SubTopic updateSubTopic(UUID id, SubTopic subTopicDetails) {
//        SubTopic subTopic = subTopicRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("SubTopic not found"));
//
//        if (!subTopic.getName().equals(subTopicDetails.getName()) &&
//            subTopicRepository.existsByNameAndTopicId(subTopicDetails.getName(), subTopic.getTopicId())) {
//            throw new RuntimeException("SubTopic name already exists in this topic");
//        }
//
//        subTopic.setName(subTopicDetails.getName());
//        return subTopicRepository.save(subTopic);
//    }
}