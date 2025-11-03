package com.seonbistudy.seonbistudy.service;

import com.seonbistudy.seonbistudy.dto.streak.StreakUpdateResult;
import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.Streak;
import com.seonbistudy.seonbistudy.service.impl.StreakServiceImpl;

public interface IStreakService {
    void initStreak(Account account);  // khởi tạo streak = 1
    StreakUpdateResult updateStreak(Account account); // cập nhật streak khi login
    Streak getStreak(Account account);  // trả về entity hoặc DTO
}