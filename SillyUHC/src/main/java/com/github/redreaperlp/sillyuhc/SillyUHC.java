package com.github.redreaperlp.sillyuhc;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import com.github.redreaperlp.sillyuhc.commands.CommandTabCompleter;
import com.github.redreaperlp.sillyuhc.commands.SillyCommand;
import com.github.redreaperlp.sillyuhc.commands.Stats;
import com.github.redreaperlp.sillyuhc.game.phases.PhaseWaiting;
import com.github.redreaperlp.sillyuhc.listener.PlayerListener;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public class SillyUHC extends JavaPlugin {
    private static SillyUHC instance;

    public static TextComponent prefix = Component.text("SillyUHC » ", TextColor.color(0xff8c00)).decorate(TextDecoration.BOLD);
    private PlayerStatsAPI api = (PlayerStatsAPI) Bukkit.getServer().getPluginManager().getPlugin("PlayerStatsAPI");
    public static World lobbyWorld;
    public static AdventureUtil adventureUtil;

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

        new PhaseWaiting(this).init();

        getConfig().getStringList("game-maps").forEach(map -> Bukkit.createWorld(new WorldCreator(map))); //TODO: Make this more efficient by loading the map when it has been voted
        lobbyWorld = Bukkit.getWorld(getConfig().getString("lobby-map", "world"));
        registerCommand("sillyuhc", new SillyCommand(this));
        registerCommand("stats", new Stats(this));
        registerListeners();
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

    //TODO: Teams/Solo <- Votable only if there is no "Host"
    //TODO: Map Voting <- On the Proxy
    //TODO: Nerfing axe and bow
    //TODO: Stats <- Databse
    //TODO: Scoreboard (One for Lobby, one for Ingame) make it so you can have public and private scoreboards
}