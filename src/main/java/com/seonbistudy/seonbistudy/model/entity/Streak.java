package com.seonbistudy.seonbistudy.model.entity;

import com.seonbistudy.seonbistudy.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "streak")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Streak extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "current_streak", nullable = false)
    private int currentStreak = 0;

    @Column(name = "max_streak", nullable = false)
    private int maxStreak = 0;

    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;

}
