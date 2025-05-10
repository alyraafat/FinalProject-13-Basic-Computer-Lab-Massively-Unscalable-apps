package com.example.moderator.repository;

import com.example.moderator.model.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {

    // Basic CRUD operations are provided by JpaRepository

    // Find all moderators for a specific community
    List<Moderator> findByCommunityId(UUID communityId);

    //Find communities that a moderator moderates
    @Query("SELECT m.communityId FROM Moderator m WHERE m.userId = :userId")
    List<UUID> findCommunityIdsByUserId(UUID userId);


    // Check if a specific user moderates a specific community
    boolean existsByUserIdAndCommunityId(UUID userId, UUID communityId);

    // Find a specific moderator record
    Optional<Moderator> findByUserIdAndCommunityId(UUID userId, UUID communityId);

    // Count moderators for a community
    long countByCommunityId(UUID communityId);
}
