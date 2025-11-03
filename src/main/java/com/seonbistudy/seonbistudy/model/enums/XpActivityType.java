package com.seonbistudy.seonbistudy.model.enums;

import lombok.Getter;

@Getter
public enum XpActivityType {
    // === Các hành động cố định ===
    ACCOUNT_REGISTER(50, "Register new account", false, false),
    DAILY_LOGIN(10, "Login daily", false, false),
    GRAMMAR_STUDY(40, "Study grammar lesson", false, false),
    LESSON_COMPLETE(25, "Complete lesson", false, false),
    QUIZ_COMPLETE(30, "Complete quiz", false, false),
    MOCK_TEST_COMPLETE(100, "Complete mock test", false, false),

    // === Các hành động động (bonus, streak) ===
    STREAK_BONUS(0, "Daily streak bonus", true, false),
    QUIZ_BONUS_GREATER_THAN_90(15, "Quiz score > 90% bonus", true, false),
    QUIZ_BONUS_GREATER_THAN_80(5, "Quiz score > 80% bonus", true, false),
    MOCK_TEST_BONUS(50, "Mock test score > 90% bonus", true, false),

    // === Các hành động tần suất cao (sử dụng Redis) ===
    VOCAB_STUDY(1, "Study new vocabulary", false, true);


    private final int defaultXp;
    private final String description;
    private final boolean isDynamic;
    private final boolean isHighFrequency;

    XpActivityType(int defaultXp, String description, boolean isDynamic, boolean isHighFrequency) {
        this.defaultXp = defaultXp;
        this.description = description;
        this.isDynamic = isDynamic;
        this.isHighFrequency = isHighFrequency;
    }

    public String getActivityName() {
        return this.name();
    }
}