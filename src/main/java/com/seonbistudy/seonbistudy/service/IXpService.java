package com.seonbistudy.seonbistudy.service;

import com.seonbistudy.seonbistudy.model.enums.XpActivityType;
import org.springframework.scheduling.annotation.Async;

public interface IXpService {
    /**
     * Cộng XP cho một hành động có giá trị XP cố định từ Enum.
     * Dùng cho các sự kiện bất đồng bộ.
     */
    @Async
    void grantXp(Long accountId, XpActivityType activity);

    /**
     * Cộng XP cho một hành động có giá trị XP được tính toán (ví dụ: streak bonus).
     * Dùng cho các sự kiện bất đồng bộ.
     */
    @Async
    void grantXp(Long accountId, int xpGained, String activityType);

    /**
     * Tích lũy XP cho các hành động tần suất rất cao (ví dụ: học từ vựng).
     * Phương thức này sẽ KHÔNG gọi DB, chỉ gọi Redis.
     */
    void incrementPendingXp(Long accountId, XpActivityType activity);
}