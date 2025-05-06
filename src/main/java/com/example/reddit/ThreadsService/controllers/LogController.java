package com.example.reddit.ThreadsService.controllers;

import com.example.reddit.ThreadsService.models.Log;
import com.example.reddit.ThreadsService.models.ActionType;
import com.example.reddit.ThreadsService.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<Log>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable UUID id) {
        return logService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Log> createLog(@RequestBody Log log) {
        return ResponseEntity.ok(logService.createLog(log));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable UUID id) {
        logService.deleteLog(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Log>> getLogsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(logService.getLogsByUser(userId));
    }

    @GetMapping("/thread/{threadId}")
    public ResponseEntity<List<Log>> getLogsByThread(@PathVariable UUID threadId) {
        return ResponseEntity.ok(logService.getLogsByThread(threadId));
    }


    @GetMapping("/action/{actionType}")
    public ResponseEntity<List<Log>> getLogsByActionType(@PathVariable ActionType actionType) {
        return ResponseEntity.ok(logService.getLogsByActionType(actionType));
    }

    @GetMapping("/user/{userId}/action/{actionType}")
    public ResponseEntity<List<Log>> getLogsByUserAndActionType(
            @PathVariable UUID userId,
            @PathVariable ActionType actionType) {
        return ResponseEntity.ok(logService.getLogsByUserAndActionType(userId, actionType));
    }
}