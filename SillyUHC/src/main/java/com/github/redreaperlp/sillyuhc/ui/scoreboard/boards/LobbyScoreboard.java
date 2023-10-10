package com.github.redreaperlp.sillyuhc.ui.scoreboard.boards;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardUI;

import java.time.LocalDateTime;

public class LobbyScoreboard extends ScoreboardUI {
    SillyUHC sillyUHC;

    public LobbyScoreboard(SillyUHC plugin) {
        super(plugin, ScoreboardWrapper.lobbyScoreboard);
        this.sillyUHC = plugin;
    }

    @Override
    public void update() {
        LocalDateTime now = LocalDateTime.now();
        setScore(15, "§e§lOnline Players:", "");
        setScore(14, "§f§l» §e" + sillyUHC.getServer().getOnlinePlayers().size()+ "§f/§e" + sillyUHC.getServer().getMaxPlayers(), "");
        setScore(13, "  ", "");
        setScore(12, "§c§lCurrent time:", "");
        setScore(11, "§f§l» §c" + String.format("%04d.%02d.%02d - %02d:%02d:%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()), "");
        setScore(10, "   ", "");
        setScore(9, "§b§lDiscord:", "");
        setScore(8, "§f§l» §b" + "/discord", "");
    }
}
