package com.seonbistudy.seonbistudy.service;

import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.Streak;

public interface IStreakService {
    void initStreak(Account account);  // khởi tạo streak = 1
    void updateStreak(Account account); // cập nhật streak khi login
    Streak getStreak(Account account);  // trả về entity hoặc DTO
}