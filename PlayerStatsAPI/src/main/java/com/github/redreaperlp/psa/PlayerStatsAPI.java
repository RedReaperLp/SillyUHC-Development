package com.github.redreaperlp.psa;

import com.github.redreaperlp.psa.database.Database;
import com.github.redreaperlp.psa.database.PlayerTracker;
import com.github.redreaperlp.psa.listener.PlayerListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatsAPI extends JavaPlugin {
    public static TextComponent getPrefix = Component.text("PlayerStatsAPI » ", TextColor.color(0xff8c00)).decorate(TextDecoration.BOLD);
    private static PlayerStatsAPI instance;
    private PlayerTracker playerTracker;
    private Database database;

    @Override
    public void onEnable() {
        instance = this;
        loadSettings();
        database = new Database("localhost", 3306, "playerstats", "root", "Reddy!1912");
        playerTracker = new PlayerTracker(this);
        playerTracker.loadOnlinePlayers();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        database.stop();
    }

    public static PlayerStatsAPI getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public PlayerTracker getPlayerTracker() {
        return playerTracker;
    }


    private void loadSettings() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            File outFile = new File(getDataFolder(), "config.yml");
            if (!outFile.exists()) {
                saveResource("config.yml", false);
            }
            List<String> aPWAP = new ArrayList<>(); // aPWAP = allowedPluginsWithAnyPermission
            addToList(aPWAP, "plugin-full-access");
            addToList(aPWAP, "plugin-coin-change");
            addToList(aPWAP, "plugin-kdwl-change");
            addToList(aPWAP, "plugin-daily-change");
            aPWAP.forEach(plugin -> PluginPermission.addAllowedPlugin(getServer().getPluginManager().getPlugin(plugin)));
        }, 1L);
    }

    public void addToList(List<String> list, String path) {
        getConfig().getStringList(path).forEach(plugin -> {
            if (!list.contains(plugin)) {
                list.add(plugin);
            }
        });
    }
}