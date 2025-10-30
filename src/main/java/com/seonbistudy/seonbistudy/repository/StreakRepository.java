package com.seonbistudy.seonbistudy.repository;

import com.seonbistudy.seonbistudy.model.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreakRepository extends JpaRepository<Streak, Long> {
    Optional<Streak> findByAccountId(Long accountId);
}
