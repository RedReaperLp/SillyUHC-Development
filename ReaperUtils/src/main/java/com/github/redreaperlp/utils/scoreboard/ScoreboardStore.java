package com.github.redreaperlp.utils.scoreboard;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;

public record ScoreboardStore(Scoreboard scoreboard, Objective objective, Map<Integer, Team> lines) {

}
