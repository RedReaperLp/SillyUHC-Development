package com.github.redreaperlp.sillyuhc.ui.scoreboard;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.game.Game;
import com.github.redreaperlp.sillyuhc.game.Phase;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.boards.PlayerScoreboard;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {

    public static Map<Player, PlayerScoreboard> scoreboards = new HashMap<>();

    public static void addPlayer(Player player) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = sb.registerNewObjective("SillyUHC", "dummy", AdventureUtil.serialize(Component.text("SillyUHC", TextColor.color(0xff8c00), TextDecoration.BOLD)));
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
        player.setScoreboard(sb);
        scoreboards.put(player, new PlayerScoreboard(SillyUHC.getInstance(), sb, objective, player));
    }

    public static void removePlayer(Player player) {
        scoreboards.remove(player);
    }

    private static Game.PhaseType lastPhase = Game.PhaseType.WAITING;

    public static void update() {
        Game currentGame = SillyUHC.getInstance().getCurrentGame();
        Phase phase = currentGame.getCurrentPhase();
        if (phase != null) PlayerScoreboard.setTimeRemaining(phase.getTimeRemaining());
        PlayerScoreboard.setPlayerCounts(Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers());
        switch (currentGame.currentPhaseType()) {
            case WAITING -> {
                if (lastPhase != Game.PhaseType.WAITING) {
                    lastPhase = Game.PhaseType.WAITING;
                    scoreboards.forEach((uuid, scoreboard) -> scoreboard.WAITING_INIT());
                    return;
                }
                scoreboards.forEach((uuid, scoreboard) -> scoreboard.WAITING());
            }
            case NO_PVP -> {
                if (lastPhase != Game.PhaseType.NO_PVP) {
                    lastPhase = Game.PhaseType.NO_PVP;
                    scoreboards.forEach((uuid, scoreboard) -> scoreboard.NO_PVP_INIT());
                    return;
                }
                scoreboards.forEach((uuid, scoreboard) -> scoreboard.NO_PVP());
            }
            case PVP -> {
                if (lastPhase != Game.PhaseType.PVP) {
                    lastPhase = Game.PhaseType.PVP;
                    scoreboards.forEach((uuid, scoreboard) -> scoreboard.PVP_INIT());
                    return;
                }
                scoreboards.forEach((uuid, scoreboard) -> scoreboard.PVP());
            }
            case END -> {
                scoreboards.forEach((uuid, scoreboard) -> scoreboard.END());
            }
        }
    }

    public static final Object update = new Object();

    private static Thread updateThread;

    public static void startUpdateThread() {
        updateThread = new Thread(() -> {
            while (!updateThread.isInterrupted()) {
                try {
                    synchronized (update) {
                        update.wait();
                    }
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        updateThread.setName("SillyUHC-Scoreboard-Update");
        updateThread.start();
    }

    public static void stopUpdateThread() {
        updateThread.interrupt();
    }
}
