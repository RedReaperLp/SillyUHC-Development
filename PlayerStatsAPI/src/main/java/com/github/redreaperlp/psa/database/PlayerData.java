package com.github.redreaperlp.psa.database;

import com.github.redreaperlp.psa.PluginPermission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private String name;
    private int coins;
    private int kills;
    private int deaths;
    private int wins;
    private int losses;
    private int dailyProgress;

    public PlayerData(Player player, int coins, int kills, int deaths, int wins, int losses, int dailyProgress) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.coins = coins;
        this.kills = kills;
        this.deaths = deaths;
        this.wins = wins;
        this.losses = losses;
        this.dailyProgress = dailyProgress;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(JavaPlugin plugin, UUID uuid) {
        this.uuid = uuid;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(JavaPlugin plugin, int coins) throws NoPermissionException {
        checkPermission(plugin, PluginPermission.PermissionType.COIN_CHANGE);
        this.coins = coins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(JavaPlugin plugin, int kills) throws NoPermissionException {
        checkPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE);
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(JavaPlugin plugin, int deaths) throws NoPermissionException {
        checkPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE);
        this.deaths = deaths;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(JavaPlugin plugin, int wins) throws NoPermissionException {
        checkPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE);
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(JavaPlugin plugin, int losses) throws NoPermissionException {
        checkPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE);
        this.losses = losses;
    }

    public int getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(JavaPlugin plugin, int dailyProgress) throws NoPermissionException {
        checkPermission(plugin, PluginPermission.PermissionType.DAILY_CHANGE);
        this.dailyProgress = dailyProgress;
    }

    public String getName() {
        return name;
    }

    public void setName(JavaPlugin plugin, String name) {
        this.name = name;
    }

    public void checkPermission(JavaPlugin plugin, PluginPermission.PermissionType type) throws NoPermissionException {
        if (!PluginPermission.hasPermission(plugin, type)) throw new NoPermissionException("No permission with plugin " + plugin.getName(), type);
    }

    public static class NoPermissionException extends Exception {
        PluginPermission.PermissionType type;
        public NoPermissionException(String message, PluginPermission.PermissionType type) {
            super(message);
            this.type = type;
        }

        public NoPermissionException(String message) {
            super(message);
        }

        public PluginPermission.PermissionType getType() {
            return type;
        }
    }
}
