package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.exception.ErrorCode;
import com.seonbistudy.seonbistudy.exception.SeonbiException;
import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.Streak;
import com.seonbistudy.seonbistudy.repository.StreakRepository;
import com.seonbistudy.seonbistudy.service.IStreakService;
import com.seonbistudy.seonbistudy.utils.TimeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StreakServiceImpl implements IStreakService {
    private final StreakRepository streakRepository;

    @Override
    public void initStreak(Account account) {
        var newStreak = Streak.builder()
                .lastActivityDate(LocalDateTime.now())
                .currentStreak(1)
                .maxStreak(1)
                .account(account)
                .build();
        streakRepository.save(newStreak);
    }

    @Override
    @Transactional
    public void updateStreak(Account account) {
        var streak = getStreak(account);

        LocalDate today = TimeUtils.today();
        LocalDate lastDay = streak.getLastActivityDate() != null
                ? streak.getLastActivityDate().atZone(TimeUtils.TIME_ZONE).toLocalDate()
                : null;

        if (lastDay == null) {
            streak.setCurrentStreak(1);
        } else {
            long days = ChronoUnit.DAYS.between(lastDay, today);
            if (days == 1) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
            } else if (days > 1) {
                streak.setCurrentStreak(1);
            }
        }

        streak.setLastActivityDate(TimeUtils.now());
        streakRepository.save(streak);
    }


    @Override
    public Streak getStreak(Account account) {
        return streakRepository.findByAccountId(account.getId())
                .orElseThrow(() -> new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND));
    }
}