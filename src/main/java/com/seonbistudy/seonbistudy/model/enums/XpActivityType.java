package com.seonbistudy.seonbistudy.model.enums;

import lombok.Getter;

@Getter
public enum XpActivityType {
    // Các hành động có XP cố định
    ACCOUNT_REGISTER(50),
    DAILY_LOGIN(10),
    GRAMMAR_STUDY(5_003),
    LESSON_COMPLETE(20),
    QUIZ_COMPLETE(25),
    MOCK_TEST_COMPLETE(100),

    // Các hành động có XP động (logic riêng)
    STREAK_BONUS(0), // Logic tính riêng
    QUIZ_BONUS(15),    // +15 nếu > 90%
    MOCK_TEST_BONUS(50), // +50 nếu > 90%

    // Các hành động tần suất cao (xử lý qua Redis)
    VOCAB_STUDY(1);

    private final int defaultXp;

    XpActivityType(int defaultXp) {
        this.defaultXp = defaultXp;
    }

    public String getActivityName() {
        return this.name();
    }
}
