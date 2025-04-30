package com.redditclone.user_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blocks", uniqueConstraints = @UniqueConstraint(
                                                name = "uq_blocks_blocker_blocked",
                                                columnNames = {"blocker_id","blocked_id"})
)
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private User blocker;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id")
    private User blocked;

    public Block(User blocker, User blocked) {
        this.blocker = blocker;
        this.blocked = blocked;
    }
}
