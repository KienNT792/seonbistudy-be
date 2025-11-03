package com.seonbistudy.seonbistudy.repository;

import com.seonbistudy.seonbistudy.model.entity.LevelThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LevelThresholdRepository extends JpaRepository<LevelThreshold, Long> {
    LevelThreshold findByLevel(int level);

    @Query(
            value = "SELECT * FROM tbl_level_thresholds WHERE xp_required <= :xp ORDER BY xp_required DESC LIMIT 1",
            nativeQuery = true
    )
    LevelThreshold findTopByCurrentXp(@Param("xp") Long xp);


    @Query("SELECT tlt FROM LevelThreshold tlt ORDER BY tlt.level ASC")
    List<LevelThreshold> getAllLevel();
}