package com.github.redreaperlp.sillyuhc.game.phases;

import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;

public interface GamePhase {
    /**
     * Called when the phase is started, you should initialize everything here including the scoreboard
     */
    void init();

    /**
     * Called when the scoreboard should be updated
     */
    boolean tick();
    void stop();


    String getName();

    String getDescription();

    ScoreboardUI getScoreboard();

    long getRemainingTime();
    void setRemainingTime(long time);
}
