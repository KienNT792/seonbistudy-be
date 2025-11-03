package com.seonbistudy.seonbistudy.service;

import com.seonbistudy.seonbistudy.model.entity.LevelThreshold;

import java.util.List;

public interface ILevelThresholdService {
    LevelThreshold getLevelByXp(Long xp);
    List<LevelThreshold> getAllLevel();
}
