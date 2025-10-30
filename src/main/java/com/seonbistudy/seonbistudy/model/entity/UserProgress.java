package com.seonbistudy.seonbistudy.model.entity;

import com.seonbistudy.seonbistudy.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_user_progress")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "level", nullable = false)
    private int level = 1;

    @Column(name = "total_xp", nullable = false)
    private Long totalXp = 0L;

    @Column(name = "xp_to_next_level", nullable = false)
    private int xpToNextLevel;
}
