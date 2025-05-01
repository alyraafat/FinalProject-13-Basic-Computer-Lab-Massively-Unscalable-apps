package com.redditclone.user_service.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        expiryDate = Instant.now().plusSeconds(60 * 60 * 24); // 2 hours
    }


}
