package com.redditclone.user_service.repositories;

import com.redditclone.user_service.models.Block;
import com.redditclone.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BlockRepository extends JpaRepository<Block, Long> {
    boolean existsByBlockerAndBlocked(User blocker, User blocked);
    void deleteByBlockerAndBlocked(User blocker, User blocked);
    @Query("SELECT b.blocked FROM Block b WHERE b.blocker.id = :blockerId")
    List<User> findBlockedUsersByBlockerId(@Param("blockerId") UUID blockerId);
    @Query("SELECT b.blocker FROM Block b WHERE b.blocked.id = :blockedId")
    List<User> findBlockersByBlockedId(@Param("blockedId") UUID blockedId);
}

