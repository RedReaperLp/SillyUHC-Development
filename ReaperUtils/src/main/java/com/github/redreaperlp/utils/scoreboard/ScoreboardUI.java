package com.github.redreaperlp.utils.scoreboard;

import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ScoreboardUI {
    private final JavaPlugin plugin;
    private final ScoreboardStore scoreboard;

    public ScoreboardUI(JavaPlugin plugin, String name, String type, Component displayName) {
        this.plugin = plugin;
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, type, AdventureUtil.serialize(displayName));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.scoreboard = new ScoreboardStore(scoreboard, objective, new HashMap<>());
    }

    public ScoreboardUI(JavaPlugin plugin, ScoreboardStore scoreboardStore) {
        this.plugin = plugin;
        this.scoreboard = scoreboardStore;
    }

    public void showPlayer(Player player) {
        if (player.getScoreboard() != scoreboard.scoreboard()) player.setScoreboard(scoreboard.scoreboard());
    }

    public void setScore(int score, String name) {
        Team team = scoreboard.lines().get(score);
        String entry = "§".repeat(score);
        if (name == null) {
            if (team != null) {
                team.unregister();
                scoreboard.lines().remove(score);
            }
            scoreboard.objective().getScoreboard().resetScores(entry);
            return;
        }
        if (team == null) {
            team = scoreboard.scoreboard().registerNewTeam("line" + score);
            team.addEntry(entry);
            scoreboard.lines().put(score, team);
        }
        team.setPrefix(name);
        scoreboard.objective().getScore(entry).setScore(score);
    }

    public void reset() {
        for (int i = 0; i < 15; i++) {
            setScore(i, null);
        }
    }

    public void showAllPlayers() {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(this::showPlayer));
    }

    public void showAllPlayers(List<Player> players) {
        Bukkit.getScheduler().runTask(plugin, () -> players.forEach(this::showPlayer));
    }

    public void hideAllPlayers() {
        hideAllPlayers(new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void hideAllPlayers(List<Player> players) {
        Bukkit.getScheduler().runTask(plugin, () -> players.forEach(this::hidePlayer));
    }

    public void hidePlayer(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }


    public ScoreboardStore getScoreboardStore() {
        return scoreboard;
    }

    public abstract void update();
}
