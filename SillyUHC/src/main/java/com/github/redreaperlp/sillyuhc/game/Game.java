package com.github.redreaperlp.sillyuhc.game;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.game.participators.Participator;
import com.github.redreaperlp.sillyuhc.game.phases.GamePhase;
import com.github.redreaperlp.sillyuhc.game.phases.PhaseEnd;
import com.github.redreaperlp.sillyuhc.game.phases.PhaseNoPvP;
import com.github.redreaperlp.sillyuhc.game.phases.PhaseStarting;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static Game currentGame;
    private SillyUHC sillyUHC;
    private BukkitTask phaseUpdater;
    private BukkitTask tickTask;
    public GamePhase currentPhase;

    List<GamePhase> phases;
    List<Participator> participators = new ArrayList<>();

    public Game(SillyUHC sillyUHC) {
        this.sillyUHC = sillyUHC;
        currentGame = this;
        phases = new ArrayList<>(List.of(new PhaseStarting(sillyUHC, 30), new PhaseNoPvP(sillyUHC, 10, "PvP"), new PhaseNoPvP(sillyUHC, 10, "End")));
    }

    public void start() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            participators.add(new Participator(player.getUniqueId(), player.getName()));
        });
        phaseUpdater = Bukkit.getScheduler().runTaskTimerAsynchronously(sillyUHC, () -> {
            if (currentPhase == null && !phases.isEmpty()) {
                currentPhase = phases.get(0);
                phases.remove(0);
                currentPhase.init();
                ScoreboardManager.next(currentPhase.getScoreboard());
            }
            if (phases.isEmpty() && currentPhase == null) stop();
            if (currentPhase != null && !currentPhase.tick()) {
                currentPhase = null;
            }
        }, 0, 20);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTasks(sillyUHC);
        new PhaseEnd(sillyUHC).init();
        currentGame = null;
    }

    /**
     * Starts a new game if there is no game running
     *
     * @param sillyUHC The plugin instance
     * @return true if a new game was started, false if there is already a game running
     */
    public static boolean startNewGame(SillyUHC sillyUHC) {
        if (currentGame != null) {
            return false;
        }
        currentGame = new Game(sillyUHC);
        currentGame.start();
        return true;
    }
}
