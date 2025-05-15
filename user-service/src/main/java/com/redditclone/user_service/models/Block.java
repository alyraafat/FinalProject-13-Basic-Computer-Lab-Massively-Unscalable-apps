package com.redditclone.user_service.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "blocks", uniqueConstraints = @UniqueConstraint(
                                                name = "uq_blocks_blocker_blocked",
                                                columnNames = {"blocker_id","blocked_id"})
)
@Builder
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User blocker;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User blocked;


    private Instant blockedDate;

    public Block(User blocker, User blocked) {
        this.blocker = blocker;
        this.blocked = blocked;
        this.blockedDate = Instant.now();
    }
}
