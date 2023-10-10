package com.github.redreaperlp.sillyuhc.game.phases;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.boards.ScoreboardWaiting;

public class PhaseEnd implements GamePhase {
    private ScoreboardUI scoreboardUI;
    private final SillyUHC sillyUHC;

    public PhaseEnd(SillyUHC sillyUHC) {
        this.sillyUHC = sillyUHC;
        scoreboardUI = new ScoreboardWaiting(sillyUHC);
    }

    @Override
    public void init() {
        scoreboardUI.update();
        scoreboardUI.showAllPlayers();
        ScoreboardManager.next(scoreboardUI);
    }

    @Override
    public boolean tick() {
        return false;
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return "End";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ScoreboardUI getScoreboard() {
        return null;
    }

    @Override
    public long getRemainingTime() {
        return 0;
    }

    @Override
    public void setRemainingTime(long time) {

    }
}
