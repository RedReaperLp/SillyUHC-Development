package com.github.redreaperlp.sillyuhc.ui.scoreboard;

public class ScoreboardManager {
    public static ScoreboardUI currentScoreboard;
    public static ScoreboardUI nextScoreboard;

    public static void update() {
        if (currentScoreboard != null) {
            currentScoreboard.update();
        }
    }

    public static void next(ScoreboardUI after) {
        currentScoreboard = nextScoreboard;
        if (currentScoreboard == null && after != null) {
            currentScoreboard = after;
        }
        nextScoreboard = after;
    }

    public ScoreboardUI getCurrentScoreboard() {
        return currentScoreboard;
    }
}
