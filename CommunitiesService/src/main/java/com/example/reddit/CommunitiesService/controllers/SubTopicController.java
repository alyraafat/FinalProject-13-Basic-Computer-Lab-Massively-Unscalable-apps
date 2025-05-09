package com.example.reddit.CommunitiesService.controllers;

import com.example.reddit.CommunitiesService.models.SubTopic;
import com.example.reddit.CommunitiesService.services.SubTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SubTopic> getSubTopicById(@PathVariable UUID id) {
        return subTopicService.getSubTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/topic/{topicId}")
//    public ResponseEntity<List<SubTopic>> getSubTopicsByTopic(@PathVariable UUID topicId) {
//        return ResponseEntity.ok(subTopicService.getSubTopicsByTopic(topicId));
//    }

    // @PostMapping
    // public ResponseEntity<SubTopic> createSubTopic(@RequestBody SubTopic subTopic) {
    //     return ResponseEntity.ok(subTopicService.createSubTopic(subTopic));
    // }

    @PostMapping
    public ResponseEntity<SubTopic> addSubTopic(
            @RequestParam String name) {
        SubTopic subTopic = subTopicService.addSubTopic(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(subTopic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTopic> updateSubTopic(
            @PathVariable UUID id,
            @RequestParam(required = true) String name) {

        SubTopic updatedSubTopic = subTopicService.updateSubTopic(id, name);
        return ResponseEntity.ok(updatedSubTopic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTopic(@PathVariable UUID id) {
        subTopicService.deleteSubTopic(id);
        return ResponseEntity.ok().build();
    }
}