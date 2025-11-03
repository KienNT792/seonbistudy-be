package com.seonbistudy.seonbistudy.dto.streak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreakUpdateResult {
    private boolean firstLoginToday;
    private int currentStreak;
    private int maxStreak;
    private String message;
}