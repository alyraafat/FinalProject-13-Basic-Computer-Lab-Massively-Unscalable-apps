package com.redditclone.thread_service.services;

import com.redditclone.thread_service.models.Comment;
import com.redditclone.thread_service.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;


    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;

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
        return commentRepository.save(comment);
    }

    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.deleteById(id);
    }

    public Comment updateComment(UUID id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        comment.setContent(commentDetails.getContent());
        return commentRepository.save(comment);
    }
}