package com.seonbistudy.seonbistudy.repository;

import com.seonbistudy.seonbistudy.model.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress,Long> {
    Optional<UserProgress> findByAccountId(Long accountId);
}
