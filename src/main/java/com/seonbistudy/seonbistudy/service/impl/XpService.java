package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.exception.ErrorCode;
import com.seonbistudy.seonbistudy.exception.SeonbiException;
import com.seonbistudy.seonbistudy.model.entity.UserProgress;
import com.seonbistudy.seonbistudy.model.enums.XpActivityType;
import com.seonbistudy.seonbistudy.repository.LevelThresholdRepository;
import com.seonbistudy.seonbistudy.repository.UserProgressRepository;
import com.seonbistudy.seonbistudy.service.IXpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class XpService implements IXpService {
    private final UserProgressRepository userProgressRepository;
    private final LevelThresholdRepository levelThresholdRepository;

    @Override
    @Async
    @Transactional
    public void grantXp(Long accountId, XpActivityType activity) {
        grantXpInternal(accountId, activity.getDefaultXp(), activity.getActivityName());
    }

    @Override
    @Async
    @Transactional
    public void grantXp(Long accountId, int xpGained, String activityType) {
        grantXpInternal(accountId, xpGained, activityType);
    }

    @Override
    public void incrementPendingXp(Long accountId, XpActivityType activity) {
//        String redisKey = "xp:pending:" + activity.getActivityName() + ":" + accountId;
//        redisTemplate.opsForValue().increment(redisKey, activity.getDefaultXp());
    }

    private void grantXpInternal(Long accountId, int xpGained, String activityType) {
        if (xpGained <= 0) {
            return;
        }

        UserProgress progress = userProgressRepository.findByAccountId(accountId)
                .orElseThrow(() -> new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND));

        long newTotalXp = progress.getTotalXp() + xpGained;
        progress.setTotalXp(newTotalXp);

        // Check xem có lên level không?
        checkAndProcessLevelUp(progress, newTotalXp);
        userProgressRepository.save(progress);

        // TODO: Ghi log

    }

    private void checkAndProcessLevelUp(UserProgress userProgress, long newTotalXp) {
        int currentLevel = userProgress.getLevel();
        Long xpRequired = levelThresholdRepository.findByLevel(currentLevel).getXpRequired();
        if (newTotalXp >= xpRequired) {
            userProgress.setLevel(currentLevel + 1);
        }
    }
}
