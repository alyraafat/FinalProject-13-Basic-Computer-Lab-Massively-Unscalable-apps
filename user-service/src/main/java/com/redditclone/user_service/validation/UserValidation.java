package com.redditclone.user_service.validation;

import com.redditclone.user_service.dtos.RegisterObject;
import com.redditclone.user_service.exceptions.RedditAppArgumentException;
import com.redditclone.user_service.exceptions.RedditAppException;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class UserValidation {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    // Inject the Redisson BloomFilter bean
    private final RBloomFilter<String> userCredentialsBloomFilter;

    public void validateUserDTO(RegisterObject registerObject) {
        // ... (this method remains the same)
        if (registerObject.getEmail() == null || registerObject.getEmail().isEmpty()) {
            throw new RedditAppException("Email cannot be null or empty");
        }
        if (registerObject.getUsername() == null || registerObject.getUsername().isEmpty()) {
            throw new RedditAppException("Username cannot be null or empty");
        }
        if (registerObject.getPassword() == null || registerObject.getPassword().isEmpty()) {
            throw new RedditAppException("Password cannot be null or empty");
        }
    }

    public boolean validateUserNonExistent(String username, String email) {
        // Step 1: Check against the shared Redis Bloom Filter first.
        // Note: Redisson's method is called 'contains', not 'mightContain'.
        boolean usernameMightExist = userCredentialsBloomFilter.contains(username);
        boolean emailMightExist = userCredentialsBloomFilter.contains(email);

        // If neither might exist according to the filter, we are certain they don't.
        // This prevents a DB query for most new user registrations.
        if (!usernameMightExist && !emailMightExist) {
           log.info("Bloom filter indicates that neither username '{}' nor email '{}' exists. No database query needed.", username, email);
            return false;
        }

        // Step 2: If the filter suspects an item exists, we must query the database to be certain.
        Optional<User> existingUserByUsername = usernameMightExist ? userRepository.findByUsername(username) : Optional.empty();
        Optional<User> existingUserByEmail = emailMightExist ? userRepository.findByEmail(email) : Optional.empty();

        boolean usernameExists = existingUserByUsername.isPresent();
        boolean emailExists = existingUserByEmail.isPresent();

        // Step 3: Apply original validation logic
        if (emailExists && usernameExists && existingUserByEmail.equals(existingUserByUsername)) {
            handleExistingUser(existingUserByEmail.get(), email, "email");
            return true; // User exists but is not enabled
        } else if (emailExists) {
            throw new RedditAppArgumentException("Email already in use");
        } else if (usernameExists) {
            throw new RedditAppArgumentException("Username already in use");
        }

        return false;
    }
    // username 1 & email 1 -> in db // hypothesis

    // username 1 & email 2 -> throw username already exist

    // username 2 & email 1 ->throw email already exist

    // username 2 & email 2 -> no exception

    // username 1 & email 1  -> not enabled -> check token
    // username 1 & email 1  -> not enabled -> token expired -> create new token -> do not add to db
    // username 1 & email 1  -> not enabled -> token not expired -> throw token exist
    // username 1 & email 1  -> enabled -> throw email already exist

    private void handleExistingUser(User existingUser, String identifier, String fieldName) {
        if (existingUser.isEnabled()) {
            throw new RedditAppException(
                    String.format("User with %s %s already exists", fieldName, identifier)
            );
        }
        // User exists but is not activated: check for a valid (unexpired) verification token
        verificationTokenRepository
                .findFirstByUserAndExpiryDateAfterOrderByExpiryDateDesc(existingUser, Instant.now())
                .ifPresent(t -> {
                    throw new RedditAppArgumentException(
                            "A verification link was already sent. Please check your email."
                    );
                });
    }
}

