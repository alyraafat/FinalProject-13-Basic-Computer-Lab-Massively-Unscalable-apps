package com.redditclone.user_service.validation;


import com.redditclone.user_service.dtos.RegisterObject;
import com.redditclone.user_service.exceptions.RedditAppArgumentException;
import com.redditclone.user_service.exceptions.RedditAppException;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;


@Component
@AllArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public void validateUserDTO(RegisterObject registerObject) {
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

    public boolean validateUserNonExistent(String userName, String email) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(email);
        Optional<User> existingUserByUsername = userRepository.findByUsername(userName);
        boolean emailExists = existingUserByEmail.isPresent();
        boolean usernameExists = existingUserByUsername.isPresent();
        if(emailExists && usernameExists && existingUserByEmail.equals(existingUserByUsername)) {
            handleExistingUser(existingUserByEmail.get(), email, "email");
            return true;
        }else if (emailExists) {
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

