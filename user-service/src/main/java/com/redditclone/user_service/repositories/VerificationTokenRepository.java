package com.redditclone.user_service.repositories;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByToken(String token);

    Optional<VerificationToken> findFirstByUserAndExpiryDateAfterOrderByExpiryDateDesc(User user, Instant now);

}
