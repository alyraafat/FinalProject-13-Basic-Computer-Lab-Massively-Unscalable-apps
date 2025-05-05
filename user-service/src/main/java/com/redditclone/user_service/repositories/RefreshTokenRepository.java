package com.redditclone.user_service.repositories;

import com.redditclone.user_service.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Query(value = "select t from RefreshToken t where t.user.id = :id and t.expired = false and t.revoked = false")
    List<RefreshToken> findAllValidRefreshTokenByUser(UUID id);

}
