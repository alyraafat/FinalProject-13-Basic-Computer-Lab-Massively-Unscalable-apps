//package com.example.moderator.command.impl;
//
//import com.example.moderator.command.Command;
//import com.example.moderator.clients.CommentsServiceClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//public class RemoveCommentCommand implements Command<Void> {
//
////    private final CommentsServiceClient commentsServiceClient;
//
//    private UUID commentId;
//    private UUID communityId;
//    private UUID moderatorId;
//
////    public RemoveCommentCommand(CommentsServiceClient commentsServiceClient) {
////        this.commentsServiceClient = commentsServiceClient;
////    }
//
//    public RemoveCommentCommand prepare(UUID commentId, UUID communityId, UUID moderatorId) {
//        this.commentId = commentId;
//        this.communityId = communityId;
//        this.moderatorId = moderatorId;
//        return this;
//    }
//
//    @Override
//    public Void execute() {
////        try {
////            commentsServiceClient.deleteComment(commentId, moderatorId, communityId);
////            return null;
////        } catch (Exception e) {
////            throw new CommentDeletionFailedException(commentId, e);
////        }
//        return null;
//    }
//
//    public static class CommentDeletionFailedException extends RuntimeException {
//        public CommentDeletionFailedException(UUID commentId, Throwable cause) {
//            super("Failed to delete comment " + commentId + ": " + cause.getMessage(), cause);
//        }
//    }
//}