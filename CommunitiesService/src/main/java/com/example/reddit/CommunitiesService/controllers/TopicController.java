package com.example.reddit.CommunitiesService.controllers;

import com.example.reddit.CommunitiesService.models.SubTopic;
import com.example.reddit.CommunitiesService.models.Topic;
import com.example.reddit.CommunitiesService.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable UUID id) {
        return topicService.getTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // @PostMapping
    // public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
    //     return ResponseEntity.ok(topicService.createTopic(topic));
    // }

    @PostMapping
    public ResponseEntity<Topic> addTopic(@RequestParam String name) {
        Topic topic = topicService.addTopic(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(
            @PathVariable UUID id,
            @RequestParam(required = true) String name) {

        Topic updatedTopic = topicService.updateTopic(id, name);
        return ResponseEntity.ok(updatedTopic);
    }

    @PostMapping("/with-subtopics")
    public ResponseEntity<Topic> createTopicWithSubtopics(
            @RequestParam String name,
            @RequestParam List<UUID> subtopicIds) {
        Topic topic = topicService.createTopicWithSubtopics(name, subtopicIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable UUID id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok().build();
    }

//   @PostMapping("/{topicId}/communities/{communityId}")
//   public ResponseEntity<Topic> addCommunity(@PathVariable UUID topicId, @PathVariable UUID communityId) {
//       return ResponseEntity.ok(topicService.addCommunity(topicId, communityId));
//   }
//
//   @DeleteMapping("/{topicId}/communities/{communityId}")
//   public ResponseEntity<Topic> removeCommunity(@PathVariable UUID topicId, @PathVariable UUID communityId) {
//       return ResponseEntity.ok(topicService.removeCommunity(topicId, communityId));
//   }

   @PostMapping("/{topicId}/subtopics/{subtopicId}")
   public ResponseEntity<Topic> addSubtopic(@PathVariable UUID topicId, @PathVariable UUID subtopicId) {
       return ResponseEntity.ok(topicService.addSubtopic(topicId, subtopicId));
   }

   @DeleteMapping("/{topicId}/subtopics/{subtopicId}")
   public ResponseEntity<Topic> removeSubtopic(@PathVariable UUID topicId, @PathVariable UUID subtopicId) {
       return ResponseEntity.ok(topicService.removeSubtopic(topicId, subtopicId));
   }

    @GetMapping("/name/{name}")
    public ResponseEntity<Topic> getTopicByName(@PathVariable String name) {
        Topic topic = topicService.getTopicByName(name);
        return topic != null ? ResponseEntity.ok(topic) : ResponseEntity.notFound().build();
    }

//    @GetMapping("/community/{communityId}")
//    public ResponseEntity<Topic> getTopicByCommunity(@PathVariable UUID communityId) {
//        Topic topic = topicService.getTopicByCommunity(communityId);
//        return topic != null ? ResponseEntity.ok(topic) : ResponseEntity.notFound().build();
//    }
}