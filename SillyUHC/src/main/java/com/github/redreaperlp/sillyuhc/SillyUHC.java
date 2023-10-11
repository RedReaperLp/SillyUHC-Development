package com.github.redreaperlp.sillyuhc;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import com.github.redreaperlp.sillyuhc.commands.CommandTabCompleter;
import com.github.redreaperlp.sillyuhc.commands.SillyCommand;
import com.github.redreaperlp.sillyuhc.commands.Stats;
import com.github.redreaperlp.sillyuhc.listener.PlayerListener;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SillyUHC extends JavaPlugin {
    private static SillyUHC instance;

    public static Component prefix = Component.text("UHC » ", TextColor.color(0xff8c00), TextDecoration.BOLD);
    private PlayerStatsAPI api = (PlayerStatsAPI) Bukkit.getServer().getPluginManager().getPlugin("PlayerStatsAPI");
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
    }

    public PlayerStatsAPI getPSA() {
        return api;
    }

    //TODO: Teams/Solo <- Votable only if there is no "Host"
    //TODO: Map Voting
    //TODO: Nerfing [cross]- bow
    //TODO: Scoreboard (One for Lobby, one for Ingame) make it so you can have public and private scoreboards
}