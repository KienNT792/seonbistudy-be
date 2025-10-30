package com.seonbistudy.seonbistudy.service;

import org.springframework.scheduling.annotation.Async;

public interface IStreakService {
    @Async
    void updateStreak(Long accountId);
}