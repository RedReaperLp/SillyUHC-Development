package com.github.redreaperlp.psa;

import com.github.redreaperlp.psa.database.Database;
import com.github.redreaperlp.psa.database.PlayerTracker;
import com.github.redreaperlp.psa.listener.PlayerListener;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatsAPI extends JavaPlugin {
    private static PlayerStatsAPI instance;
    private PlayerTracker playerTracker;
    private Database database;
    public static AdventureUtil adventureUtil;

    @Override
    public void onEnable() {
        instance = this;
        adventureUtil = new AdventureUtil(Component.text("PlayerStatsAPI » ", TextColor.color(0xff8c00)).decorate(TextDecoration.BOLD));
        loadSettings();
        database = new Database(getConfig().getString("database.host"),
                getConfig().getInt("database.port"),
                getConfig().getString("database.database"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password"));
        playerTracker = new PlayerTracker(this);
        playerTracker.loadOnlinePlayers();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.stop();
        }
    }

    public static PlayerStatsAPI getInstance() {
        return instance;
    }

    public Database getDatabase(Plugin plugin) {
        PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.DATABASE_ACCESS);
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
            addToList(aPWAP, "plugin.full-access");
            addToList(aPWAP, "plugin.coin-change");
            addToList(aPWAP, "plugin.kdwl-change");
            addToList(aPWAP, "plugin.daily-change");
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