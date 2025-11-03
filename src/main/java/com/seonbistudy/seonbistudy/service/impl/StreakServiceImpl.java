package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.dto.streak.StreakUpdateResult;
import com.seonbistudy.seonbistudy.exception.ErrorCode;
import com.seonbistudy.seonbistudy.exception.SeonbiException;
import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.Streak;
import com.seonbistudy.seonbistudy.repository.StreakRepository;
import com.seonbistudy.seonbistudy.service.IStreakService;
import com.seonbistudy.seonbistudy.utils.TimeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StreakServiceImpl implements IStreakService {
    private final StreakRepository streakRepository;
    private final MessageSource messageSource;

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
    public StreakUpdateResult updateStreak(Account account) {
        var streak = getStreak(account);

        var today = TimeUtils.today();
        var lastDay = streak.getLastActivityDate() != null
                ? streak.getLastActivityDate().atZone(TimeUtils.TIME_ZONE).toLocalDate()
                : null;

        String message;

        if (lastDay == null) {
            streak.setCurrentStreak(1);
            message = messageSource.getMessage("streak.new", null, LocaleContextHolder.getLocale());
        } else {
            long days = ChronoUnit.DAYS.between(lastDay, today);
            if (days == 1) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                message = messageSource.getMessage("streak.increment",
                        new Object[]{streak.getCurrentStreak()}, LocaleContextHolder.getLocale());
            } else if (days > 1) {
                streak.setCurrentStreak(1);
                message = messageSource.getMessage("streak.reset", null, LocaleContextHolder.getLocale());
            } else {
                message = messageSource.getMessage("streak.common", null, LocaleContextHolder.getLocale());
            }
        }

        boolean firstLoginToday = (lastDay == null) || !lastDay.isEqual(today);
        streak.setLastActivityDate(TimeUtils.now());
        streakRepository.save(streak);

        return new StreakUpdateResult(firstLoginToday,
                streak.getCurrentStreak(),
                streak.getMaxStreak(),
                message);
    }

    @Override
    public Streak getStreak(Account account) {
        return streakRepository.findByAccountId(account.getId())
                .orElseThrow(() -> new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND));
    }
}