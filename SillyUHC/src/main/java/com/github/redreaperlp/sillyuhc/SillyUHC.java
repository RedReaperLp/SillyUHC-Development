package com.github.redreaperlp.sillyuhc;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import com.github.redreaperlp.sillyuhc.commands.CommandTabCompleter;
import com.github.redreaperlp.sillyuhc.commands.SillyCommand;
import com.github.redreaperlp.sillyuhc.commands.Stats;
import com.github.redreaperlp.sillyuhc.listener.PlayerListener;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.boards.LobbyScoreboard;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class SillyUHC extends JavaPlugin {
    private static SillyUHC instance;

    public static Component prefix = Component.text("UHC » ", TextColor.color(0xff8c00), TextDecoration.BOLD);
    private PlayerStatsAPI api = (PlayerStatsAPI) Bukkit.getServer().getPluginManager().getPlugin("PlayerStatsAPI");
    public static World lobbyWorld;
    public static AdventureUtil adventureUtil;
    LobbyScoreboard lobbyScoreboard;


    @Override
    public void onEnable() {
        instance = this;
        adventureUtil = new AdventureUtil(prefix);
        if (api != null) {
            if (api.isEnabled()) {
                adventureUtil.sendWithPrefix(Component.text("PlayerStatsAPI found!", TextColor.color(0x00ff00)), Bukkit.getConsoleSender());
            } else {
                adventureUtil.sendWithPrefix(Component.text("PlayerStatsAPI found but not enabled!", TextColor.color(0xff0000)), Bukkit.getConsoleSender());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } else {
            adventureUtil.sendWithPrefix(Component.text("PlayerStatsAPI not found!", TextColor.color(0xff0000)), Bukkit.getConsoleSender());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveResource("config.yml", false);

        getConfig().getStringList("game-maps").forEach(map -> Bukkit.createWorld(new WorldCreator(map))); //TODO: Make this more efficient by loading the map when it has been voted
        registerCommand("sillyuhc", new SillyCommand(this));
        registerCommand("stats", new Stats(this));
        registerListeners();

        //following will be moved to a Lobby-Plugin afterwards when we have a proxy
        lobbyWorld = Bukkit.createWorld(new WorldCreator(getConfig().getString("lobby-map", "world")));
        lobbyScoreboard = new LobbyScoreboard(this);
        if (lobbyWorld != null) {
            lobbyScoreboard.showAllPlayers(new ArrayList<>(lobbyWorld.getPlayers()));
            Bukkit.getScheduler().runTaskTimer(this, lobbyScoreboard::update, 0, 20);
        }
    }

    @Override
    public void onDisable() {

    }


    public static SillyUHC getInstance() {
        return instance;
    }


    public void registerCommand(String name, CommandTabCompleter executor) {
        getCommand(name).setExecutor(executor);
        getCommand(name).setTabCompleter(executor);
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        Bukkit.createWorld(new WorldCreator("0000map1"));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public PlayerStatsAPI getPSA() {
        return api;
    }

    public World getLobbyWorld() {
        return lobbyWorld;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return lobbyScoreboard;
    }

    //TODO: Teams/Solo <- Votable only if there is no "Host"
    //TODO: Map Voting
    //TODO: Nerfing [cross]- bow
    //TODO: Scoreboard (One for Lobby, one for Ingame) make it so you can have public and private scoreboards
}