package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.exception.ErrorCode;
import com.seonbistudy.seonbistudy.exception.SeonbiException;
import com.seonbistudy.seonbistudy.model.entity.Streak;
import com.seonbistudy.seonbistudy.repository.StreakRepository;
import com.seonbistudy.seonbistudy.service.IStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StreakServiceImpl implements IStreakService {
    private final StreakRepository streakRepository;

    @Override
    public void updateStreak(Long accountId) {
        Streak accountStreak = streakRepository.findByAccountId(accountId)
                .orElseThrow(() -> new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND));

        LocalDateTime lastLogin = accountStreak.getLastActivityDate();
        if ((lastLogin != null)
                && lastLogin.isBefore(LocalDateTime.now())) {
            accountStreak.setCurrentStreak(accountStreak.getCurrentStreak() + 1);
        } else {
            // Reset Streak
            accountStreak.setCurrentStreak(0);
        }
        accountStreak.setLastActivityDate(LocalDateTime.now());

        // if current streak more than max streak -> update max streak
        if (accountStreak.getCurrentStreak() > accountStreak.getMaxStreak()) {
            accountStreak.setMaxStreak(accountStreak.getCurrentStreak());
        }
        streakRepository.save(accountStreak);
    }
}
