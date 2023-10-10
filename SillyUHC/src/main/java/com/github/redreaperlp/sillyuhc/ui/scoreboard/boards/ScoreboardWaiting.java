package com.github.redreaperlp.sillyuhc.ui.scoreboard.boards;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;

public class ScoreboardWaiting extends ScoreboardUI {
    SillyUHC sillyUHC;

    public ScoreboardWaiting(SillyUHC sillyUHC) {
        super(sillyUHC);
        this.sillyUHC = sillyUHC;
    }

    @Override
    public void update() {
        setScore(15, "§a§lServer IP:", "");
        setScore(14, "§f§l» §a" + "SillyUHC.hosting.ethera.net", "");
        setScore(13, " ", "");
        setScore(12, "§e§lWaiting for Players!", "");
        setScore(11, "§f§l» §e" + sillyUHC.getServer().getOnlinePlayers().size() + "§f/§e" + sillyUHC.getServer().getMaxPlayers(), "");
        setScore(10, "  ", "");
        setScore(9, "§c§lMode:", "");
        setScore(8, "§f§l» §c" + "Solo", "");
        setScore(7, "   ", "");
        setScore(6, "§b§lDiscord:", "");
        setScore(5, "§f§l» §b" + "/discord", "");
        showAllPlayers();
    }
}
