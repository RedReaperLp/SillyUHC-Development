package com.github.redreaperlp.sillyuhc.ui.scoreboard.boards;

import com.github.redreaperlp.psa.database.PlayerData;
import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.game.Game;
import com.github.redreaperlp.utils.scoreboard.ScoreboardStore;
import com.github.redreaperlp.utils.scoreboard.ScoreboardUI;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class PlayerScoreboard extends ScoreboardUI {
    private static Component arrow = Component.empty().append(Component.text("» ", TextColor.color(0xffffff)).decorate(TextDecoration.BOLD));
    private final PlayerData playerData;
    private final SillyUHC sillyUHC;

    private static String timeRemMin = "00";
    private static String timeRemSec = "00";
    private static int playerCount = 0;
    private static int maxPlayerCount = 0;
    private static String map = "Seed-Generated";

    int kills = 0;
    int i = 1;


    public PlayerScoreboard(SillyUHC sillyUHC, Scoreboard sb, Objective objective, Player player) {
        super(sillyUHC, new ScoreboardStore(sb, objective, new HashMap<>()));
        this.sillyUHC = sillyUHC;
        playerData = sillyUHC.getPSA().getPlayerTracker().getPlayerData(player);
        Game game = sillyUHC.getCurrentGame();
        if (game == null) {
            WAITING_INIT();
            return;
        }
        switch (game.currentPhaseType()) {
            case WAITING -> WAITING_INIT();
            case NO_PVP -> NO_PVP_INIT();
            case PVP -> PVP_INIT();
            case END -> END();
        }
    }

    @Override
    public void update() {
    }

    public void WAITING_INIT() {
        reset();
        setScore(15, AdventureUtil.serialize(Component.text("Current State:", TextColor.color(0xffff00)).decorate(TextDecoration.BOLD)));
        setScore(14, AdventureUtil.serialize(arrow.append(Component.text("Waiting for players...", TextColor.color(0xffff00)))));
        setScore(13, " ");
        setScore(12, AdventureUtil.serialize(Component.text("Silly-Coins:", TextColor.color(0xff0000)).decorate(TextDecoration.BOLD)));
        setScore(11, AdventureUtil.serialize(arrow.append(Component.text(playerData.getCoins(), TextColor.color(0xff0000)))));
        setScore(10, " ");
        setScore(9, AdventureUtil.serialize(Component.text("Current Map: ", TextColor.color(0x00ff00)).decorate(TextDecoration.BOLD)));
        setScore(8, AdventureUtil.serialize(arrow.append(Component.text(map, TextColor.color(0x00ff00)))));
    }

    public void WAITING() {
        playerData.setCoins(sillyUHC, playerData.getCoins() + 1);
        setScore(14, AdventureUtil.serialize(arrow.append(Component.text("Waiting for players...", TextColor.color(0xffff00)))));
        setScore(11, AdventureUtil.serialize(arrow.append(Component.text(playerData.getCoins(), TextColor.color(0xff0000)))));
        setScore(8, AdventureUtil.serialize(arrow.append(Component.text(map, TextColor.color(0x00ff00)))));
    }

    public void NO_PVP_INIT() {
        reset();
        setScore(15, AdventureUtil.serialize(Component.text("Players Left: ", TextColor.color(0xffff00)).decorate(TextDecoration.BOLD)));
        setScore(14, AdventureUtil.serialize(arrow.append(Component.text(playerCount + " / " + maxPlayerCount, TextColor.color(0xffff00)))));
        setScore(13, " ");
        setScore(12, AdventureUtil.serialize(Component.text("Silly-Coins:", TextColor.color(0xff0000)).decorate(TextDecoration.BOLD)));
        setScore(11, AdventureUtil.serialize(arrow.append(Component.text(playerData.getCoins(), TextColor.color(0xff0000)))));
        setScore(10, " ");
        setScore(9, AdventureUtil.serialize(Component.text("PvP State:", TextColor.color(0x00ff00)).decorate(TextDecoration.BOLD)));
        setScore(8, AdventureUtil.serialize(arrow.append(Component.text("Off -> On in " + timeRemMin + ":" + timeRemSec, TextColor.color(0x00ff00)))));
    }

    public void NO_PVP() {
        playerData.setCoins(sillyUHC, playerData.getCoins() + 1);
        setScore(14, AdventureUtil.serialize(arrow.append(Component.text(playerCount + " / " + maxPlayerCount, TextColor.color(0xffff00)))));
        setScore(11, AdventureUtil.serialize(arrow.append(Component.text(playerData.getCoins(), TextColor.color(0xff0000)))));
        setScore(8, AdventureUtil.serialize(arrow.append(Component.text("Off -> On in " + timeRemMin + ":" + timeRemSec, TextColor.color(0x00ff00)))));
    }

    public void PVP_INIT() {
        reset();
        setScore(15, AdventureUtil.serialize(Component.text("Players Left: ", TextColor.color(0xffff00)).decorate(TextDecoration.BOLD)));
        setScore(14, AdventureUtil.serialize(arrow.append(Component.text(playerCount + " / " + maxPlayerCount, TextColor.color(0xffff00)))));
        setScore(13, " ");
        setScore(12, AdventureUtil.serialize(Component.text("Silly-Coins:", TextColor.color(0xff0000)).decorate(TextDecoration.BOLD)));
        setScore(11, AdventureUtil.serialize(arrow.append(Component.text(playerData.getCoins(), TextColor.color(0xff0000)))));
        setScore(10, " ");
        setScore(9, AdventureUtil.serialize(Component.text("Your Kills:", TextColor.color(0x00ffff)).decorate(TextDecoration.BOLD)));
        setScore(8, AdventureUtil.serialize(arrow.append(Component.text(kills, TextColor.color(0x00ffff)))));
        setScore(7, " ");
        setScore(6, AdventureUtil.serialize(Component.text("PvP:", TextColor.color(0x00ff00)).decorate(TextDecoration.BOLD)));
        setScore(5, AdventureUtil.serialize(arrow.append(Component.text("On", TextColor.color(0x00ff00)))));
    }

    public void PVP() {
        playerData.setCoins(sillyUHC, playerData.getCoins() + 1);
        setScore(14, AdventureUtil.serialize(arrow.append(Component.text(playerCount + " / " + maxPlayerCount, TextColor.color(0xffff00)))));
        setScore(11, AdventureUtil.serialize(arrow.append(Component.text(playerData.getCoins(), TextColor.color(0xff0000)))));
        setScore(8, AdventureUtil.serialize(arrow.append(Component.text(kills, TextColor.color(0x00ffff)))));
    }

    public void END() {
        reset();
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public static void setTimeRemaining(long timeRemaining) {
        PlayerScoreboard.timeRemMin = String.format("%02d", timeRemaining / 60);
        PlayerScoreboard.timeRemSec = String.format("%02d", timeRemaining % 60);
    }

    public static void setPlayerCounts(int playerCount, int maxPlayerCount) {
        PlayerScoreboard.playerCount = playerCount;
        PlayerScoreboard.maxPlayerCount = maxPlayerCount;
    }
}
