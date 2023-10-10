package com.github.redreaperlp.sillyuhc.game.phases;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.boards.ScoreboardWaiting;

public class PhaseWaiting implements GamePhase {
    private ScoreboardWaiting scoreboardUI;
    private SillyUHC sillyUHC;


    public PhaseWaiting(SillyUHC sillyUHC) {
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
    public void stop() {

    }

    @Override
    public boolean tick() {
        scoreboardUI.update();
        return true;
    }

    @Override
    public String getName() {
        return "Waiting";
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
        return Long.MAX_VALUE;
    }

    @Override
    public void setRemainingTime(long time) {
    }
}
