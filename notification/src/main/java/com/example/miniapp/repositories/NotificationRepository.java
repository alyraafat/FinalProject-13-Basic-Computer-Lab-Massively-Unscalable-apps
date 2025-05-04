//package com.example.miniapp.repositories;
//
//import com.example.miniapp.models.Notification;
//import com.example.miniapp.models.NotificationType;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//
//    // Basic queries
//    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
//
//    List<Notification> findByUserIdAndDeliveredFalse(String userId);
//
//    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.type.category = :category")
//    Page<Notification> findByUserAndCategory(
//            @Param("userId") String userId,
//            @Param("category") String category,
//            Pageable pageable);
//
//     Count queries
//    long countByUserIdAndReadFalse(String userId);
//
//    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.type = :type")
//    long countByUserAndType(
//            @Param("userId") String userId,
//            @Param("type") NotificationType type);
//
//    // Batch operations
//    @Modifying
//    @Query("UPDATE Notification n SET n.read = true WHERE n.userId = :userId AND n.id IN :ids")
//    int markAsRead(@Param("userId") String userId, @Param("ids") List<Long> ids);
//
//    @Modifying
//    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
//    int deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
//}