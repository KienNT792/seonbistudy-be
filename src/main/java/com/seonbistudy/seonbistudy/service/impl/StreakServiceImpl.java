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
            message = getStreakMessage(1);
        } else {
            long days = ChronoUnit.DAYS.between(lastDay, today);
            if (days == 1) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                if (streak.getCurrentStreak() > streak.getMaxStreak()) {
                    streak.setMaxStreak(streak.getCurrentStreak());
                }
                message = getStreakMessage(streak.getCurrentStreak());
            } else if (days > 1) {
                streak.setCurrentStreak(1);
                message = messageSource.getMessage("streak.lost", null, LocaleContextHolder.getLocale());
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

    private String getStreakMessage(int currentStreak) {
        String messageKey;

        // Check for specific milestone days
        if (currentStreak == 1) {
            messageKey = "streak.day.1";
        } else if (currentStreak == 2) {
            messageKey = "streak.day.2";
        } else if (currentStreak == 3) {
            messageKey = "streak.day.3";
        } else if (currentStreak == 5) {
            messageKey = "streak.day.5";
        } else if (currentStreak == 7) {
            messageKey = "streak.day.7";
        } else if (currentStreak == 10) {
            messageKey = "streak.day.10";
        } else if (currentStreak == 14) {
            messageKey = "streak.day.14";
        } else if (currentStreak == 21) {
            messageKey = "streak.day.21";
        } else if (currentStreak == 30) {
            messageKey = "streak.day.30";
        } else if (currentStreak == 50) {
            messageKey = "streak.day.50";
        } else if (currentStreak == 60) {
            messageKey = "streak.day.60";
        } else if (currentStreak == 90) {
            messageKey = "streak.day.90";
        } else if (currentStreak == 100) {
            messageKey = "streak.day.100";
        } else {
            // For other days, use generic messages with rotation
            int messageVariant = currentStreak % 3;
            if (messageVariant == 0) {
                messageKey = "streak.generic";
            } else if (messageVariant == 1) {
                messageKey = "streak.generic.alt1";
            } else {
                messageKey = "streak.generic.alt2";
            }
        }

        return messageSource.getMessage(messageKey, new Object[]{currentStreak}, LocaleContextHolder.getLocale());
    }
}