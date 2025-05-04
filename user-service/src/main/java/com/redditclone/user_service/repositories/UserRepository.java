package com.redditclone.user_service.repositories;


import com.redditclone.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND u.id NOT IN :blockedUserIds")
    List<User> searchUsersExcludingBlocked(String keyword, List<UUID> blockedUserIds);

    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);

}
