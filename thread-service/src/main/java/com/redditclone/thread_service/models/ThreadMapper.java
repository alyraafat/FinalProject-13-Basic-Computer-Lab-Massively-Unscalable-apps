package com.redditclone.thread_service.models;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ThreadMapper {
    public static Thread toEntity(ThreadDto dto) {
        return new Thread.Builder()
                .id(dto.getId() != null ? dto.getId() : UUID.randomUUID()) // Generate if not provided
                .topic(dto.getTopic())
                .title(dto.getTitle())
                .content(dto.getContent())
                .authorId(dto.getAuthorId())
                .comments(dto.getComments())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .upVotes(dto.getUpVotes() != null ? dto.getUpVotes() : 0)
                .downVotes(dto.getDownVotes() != null ? dto.getDownVotes() : 0)
                .communityId(dto.getCommunityId())
                .build();
    }

    public static ThreadDto toDto(Thread thread) {
        ThreadDto dto = new ThreadDto();
        dto.setId(thread.getId());
        dto.setTopic(thread.getTopic());
        dto.setTitle(thread.getTitle());
        dto.setContent(thread.getContent());
        dto.setAuthorId(thread.getAuthorId());
        dto.setComments(thread.getComments());
        dto.setCreatedAt(thread.getCreatedAt());
        dto.setUpVotes(thread.getUpVotes());
        dto.setDownVotes(thread.getDownVotes());
        dto.setCommunityId(thread.getCommunityId());
        return dto;
    }
}
