package com.example.reddit.CommunitiesService.controllers;

import com.example.reddit.CommunitiesService.models.SubTopic;
import com.example.reddit.CommunitiesService.services.SubTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subtopics")
public class SubTopicController {
    private final SubTopicService subTopicService;

    @Autowired
    public SubTopicController(SubTopicService subTopicService) {
        this.subTopicService = subTopicService;
    }

    @GetMapping
    public ResponseEntity<List<SubTopic>> getAllSubTopics() {
        return ResponseEntity.ok(subTopicService.getAllSubTopics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTopic> getSubTopicById(@PathVariable String id) {
        return subTopicService.getSubTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<SubTopic>> getSubTopicsByTopic(@PathVariable String topicId) {
        return ResponseEntity.ok(subTopicService.getSubTopicsByTopic(topicId));
    }

//    @PostMapping
//    public ResponseEntity<SubTopic> createSubTopic(@RequestBody SubTopic subTopic) {
//        return ResponseEntity.ok(subTopicService.createSubTopic(subTopic));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<SubTopic> updateSubTopic(
//            @PathVariable UUID id,
//            @RequestBody SubTopic subTopicDetails) {
//        return ResponseEntity.ok(subTopicService.updateSubTopic(id, subTopicDetails));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSubTopic(@PathVariable UUID id) {
//        subTopicService.deleteSubTopic(id);
//        return ResponseEntity.ok().build();
//    }
}