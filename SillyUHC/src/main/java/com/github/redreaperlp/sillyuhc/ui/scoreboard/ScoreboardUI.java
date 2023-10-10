package com.github.redreaperlp.sillyuhc.ui.scoreboard;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

import static com.github.redreaperlp.sillyuhc.SillyUHC.adventureUtil;

public abstract class ScoreboardUI {
    private SillyUHC plugin;
    private static Scoreboard scoreboard;
    private static Objective objective;
    private static Map<Integer, Team> lines = new HashMap<>();

    public ScoreboardUI(SillyUHC plugin) {
        this.plugin = plugin;
        if (scoreboard == null) {
            scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
            objective = scoreboard.registerNewObjective("SillyUHC", Criteria.DUMMY, adventureUtil.serialize(Component.text("SillyUHC", TextColor.color(0xff8c00)).decorate(TextDecoration.BOLD)));
            objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
        }
    }

    public void showPlayer(Player player) {
        if (player.getScoreboard() != scoreboard) player.setScoreboard(scoreboard);
    }

    public void setScore(int score, String name, String value) {
        Team team = lines.get(score);
        String entry = " ".repeat(score);
        if (name == null) {
            if (team != null) {
                team.unregister();
                lines.remove(score);
            }
            objective.getScoreboard().resetScores(entry);
            return;
        }
        if (team == null) {
            team = scoreboard.registerNewTeam("line" + score);
            team.addEntry(entry);
            lines.put(score, team);
        }
        team.setPrefix(name);
        team.setSuffix(value);
        objective.getScore(entry).setScore(score);
    }

    public void showAllPlayers() {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(this::showPlayer));
    }

    public abstract void update();
}
