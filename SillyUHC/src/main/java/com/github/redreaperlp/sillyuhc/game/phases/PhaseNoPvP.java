package com.github.redreaperlp.sillyuhc.game.phases;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.boards.ScorboardIngame;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;

public class PhaseNoPvP implements GamePhase {
    ScorboardIngame sb;
    long remainingTime;
    SillyUHC sillyUHC;

    public PhaseNoPvP(SillyUHC sillyUHC, long remainingTime, String nextPhase) {
        this.sillyUHC = sillyUHC;
        this.remainingTime = remainingTime;
        sb = new ScorboardIngame(sillyUHC, remainingTime);
        sb.setNextPhase(nextPhase);
    }

    @Override
    public void init() {
        sb.update();
        sb.showAllPlayers();
    }

    @Override
    public boolean tick() {
        sb.setRemainingTime(remainingTime);
        sb.update();
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
        return sb;
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
