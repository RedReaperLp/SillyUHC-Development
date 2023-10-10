package com.github.redreaperlp.sillyuhc.ui.scoreboard.boards;

import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class ScoreboardWrapper {
    public final static ScoreboardStore gameScoreboard = createScoreboardStore("GameBoard", Component.text("SillyUHC", TextColor.color(0xff8c00), TextDecoration.BOLD));
    public final static ScoreboardStore lobbyScoreboard = createScoreboardStore("LobbyBoard", Component.text("Silly Game Events", TextColor.color(0xff8c00), TextDecoration.BOLD));


    public static ScoreboardStore createScoreboardStore(String name, Component title) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, Criteria.DUMMY, AdventureUtil.serialize(title));
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
        return new ScoreboardStore(scoreboard, objective, new HashMap<>());
    }


}
