package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.model.entity.LevelThreshold;
import com.seonbistudy.seonbistudy.repository.LevelThresholdRepository;
import com.seonbistudy.seonbistudy.service.ILevelThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelThresholdService implements ILevelThresholdService {

    private final LevelThresholdRepository levelThresholdRepository;

    @Override
    public LevelThreshold getLevelByXp(Long xp) {
        return levelThresholdRepository.findTopByCurrentXp(xp);
    }

    @Override
    public List<LevelThreshold> getAllLevel() {
        return levelThresholdRepository.getAllLevel();
    }
}