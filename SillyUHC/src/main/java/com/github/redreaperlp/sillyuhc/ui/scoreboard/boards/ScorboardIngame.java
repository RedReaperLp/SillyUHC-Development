package com.github.redreaperlp.sillyuhc.ui.scoreboard.boards;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;
import org.bukkit.Bukkit;

public class ScorboardIngame extends ScoreboardUI {
    String gameState = "No-PVP";
    String nextPhase = "PVP";
    long nextPhaseTime;

    public ScorboardIngame(SillyUHC plugin, long nextPhaseTime) {
        super(plugin);
        this.nextPhaseTime = nextPhaseTime;
    }

    public void update() {
        String parsedTimeUntilNextPhase = String.format("%02d:%02d", nextPhaseTime / 60, nextPhaseTime % 60);
        setScore(15, "§a§lGame State:", "");
        setScore(14, "§f§l» §a" + gameState, "");
        setScore(13, " ", "");
        if (nextPhaseTime > 0) {
            setScore(9, "§e§lUntil next phase:", "");
            setScore(8, "§f§l» §e" + parsedTimeUntilNextPhase + " (" + nextPhase + ")", "");
            setScore(7, "  ", "");
        } else {
            setScore(9, null, "");
            setScore(8, null, "");
            setScore(7, null, "");
        }
        setScore(12, "§c§lPlayers Online:", "");
        setScore(11, "§f§l» §c" + Bukkit.getOnlinePlayers().size() + "§f/§c" + Bukkit.getServer().getMaxPlayers(), "");
        setScore(10, "  ", "");
        setScore(6, "§e§lServer IP:", "");
        setScore(5, "§f§l» §e" + "SillyUHC.hosting.ethera.net", "");
        showAllPlayers();
    }

    public void setNextPhase(String nextPhase) {
        this.nextPhase = nextPhase;
    }

    public void setRemainingTime(long remainingTime) {
        this.nextPhaseTime = remainingTime;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public long remainingTime() {
        return nextPhaseTime;
    }
}
