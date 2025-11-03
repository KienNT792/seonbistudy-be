package com.seonbistudy.seonbistudy.service;

import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.UserProgress;
import com.seonbistudy.seonbistudy.model.enums.XpActivityType;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;

public interface IXpService {
    @Async
    void grantXp(Long accountId, XpActivityType activity);

    @Async
    void grantXp(Long accountId, int xpGained, String activityType);

    void incrementPendingXp(Long accountId, XpActivityType activity);

    UserProgress getProgress(Account account);
    UserProgress initProgress(Account account);
}