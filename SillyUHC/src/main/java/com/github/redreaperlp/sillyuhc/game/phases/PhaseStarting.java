package com.github.redreaperlp.sillyuhc.game.phases;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.boards.ScoreboardStarting;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;

public class PhaseStarting implements GamePhase {
    private SillyUHC sillyUHC;
    private ScoreboardStarting scoreboardUI;
    private long remainingTime;

    public PhaseStarting(SillyUHC sillyUHC, long remainingTime) {
        this.sillyUHC = sillyUHC;
        this.remainingTime = remainingTime;
        scoreboardUI = new ScoreboardStarting(sillyUHC, remainingTime);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean tick() {
        scoreboardUI.setTimeRemaining(remainingTime);
        scoreboardUI.update();
        remainingTime--;
        return remainingTime > 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ScoreboardUI getScoreboard() {
        return scoreboardUI;
    }

    @Override
    public long getRemainingTime() {
        return remainingTime;
    }

    @Override
    public void setRemainingTime(long time) {
        remainingTime = time;
    }
}
