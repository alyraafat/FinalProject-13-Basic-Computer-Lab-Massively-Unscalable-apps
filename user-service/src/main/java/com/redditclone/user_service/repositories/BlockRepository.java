package com.redditclone.user_service.repositories;

import com.redditclone.user_service.models.Block;
import com.redditclone.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    boolean existsByBlockerAndBlocked(User blocker, User blocked);
    void deleteByBlockerAndBlocked(User blocker, User blocked);
    List<Block> findByBlocker(User blocker);
}

