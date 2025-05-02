//package com.example.moderator.clients;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//import java.util.UUID;
//
//@FeignClient(name = "comments-service", url = "${services.comments.url}")
//public interface CommentsServiceClient {
//    @DeleteMapping("/api/comments/{commentId}/moderate")
//    void deleteComment(
//            @PathVariable("commentId") UUID commentId,
//            @RequestHeader("X-Moderator-Id") UUID moderatorId,
//            @RequestHeader("X-Community-Id") UUID communityId
//    );
//}