package com.example.reddit.ThreadsService.services;

import com.example.reddit.ThreadsService.models.Comment;
import com.example.reddit.ThreadsService.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ThreadService threadService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ThreadService threadService) {
        this.commentRepository = commentRepository;
        this.threadService = threadService;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(UUID id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getCommentsByThread(UUID threadId) {
        return commentRepository.findByThreadId(threadId);
    }

    public Comment createComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        threadService.addComment(comment.getThreadId(), savedComment.getId());
        return savedComment;
    }

    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
        threadService.removeComment(comment.getThreadId(), id);
        commentRepository.deleteById(id);
    }

    public Comment updateComment(UUID id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        comment.setContent(commentDetails.getContent());
        return commentRepository.save(comment);
    }
}