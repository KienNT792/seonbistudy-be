package com.seonbistudy.seonbistudy.repository;

import com.seonbistudy.seonbistudy.model.entity.LevelThreshold;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelThresholdRepository extends JpaRepository<LevelThreshold,Long> {
    LevelThreshold findByLevel(int level);
}
