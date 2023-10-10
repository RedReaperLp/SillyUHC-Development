package com.github.redreaperlp.sillyuhc.ui.scoreboard.boards;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;

public class ScoreboardStarting extends ScoreboardUI {
    private long remainingTime;
    private SillyUHC sillyUHC;
    public ScoreboardStarting(SillyUHC sillyUHC, long remainingTime) {
        super(sillyUHC, ScoreboardWrapper.gameScoreboard);
        this.sillyUHC = sillyUHC;
        this.remainingTime = remainingTime;
    }

    @Override
    public void update() {
        setScore(15, "§a§lStarting!", "");
        setScore(14, "§f§l» §a" + String.format("%02d:%02d", remainingTime / 60, remainingTime % 60), "");
        setScore(13, " ", "");
        setScore(12, "§e§lPlayers Online:", "");
        setScore(11, "§f§l» §e" + sillyUHC.getServer().getOnlinePlayers().size() + "§f/§e" + sillyUHC.getServer().getMaxPlayers(), "");
        setScore(10, "  ", "");
        setScore(9, "§c§lServer IP:", "");
        setScore(8, "§f§l» §c" + "SillyUHC.hosting.ethera.net", "");
        setScore(7, "   ", "");
        setScore(6, "§b§lDiscord:", "");
        setScore(5, "§f§l» §b" + "/discord", "");
        showAllPlayers();
    }

    public void setTimeRemaining(long remainingTime) {
        this.remainingTime = remainingTime;
    }
}
