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
        if (PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.COIN_CHANGE)) this.coins = coins;
        else throw new NoPermissionException("No permission with plugin " + plugin.getName(), PluginPermission.PermissionType.COIN_CHANGE);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(JavaPlugin plugin, int kills) throws NoPermissionException {
        if (PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE)) this.kills = kills;
        else throw new NoPermissionException("No permission with plugin " + plugin.getName(), PluginPermission.PermissionType.KDWL_CHANGE);
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(JavaPlugin plugin, int deaths) throws NoPermissionException {
        if (PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE)) this.deaths = deaths;
        else throw new NoPermissionException("No permission with plugin " + plugin.getName(), PluginPermission.PermissionType.KDWL_CHANGE);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(JavaPlugin plugin, int wins) throws NoPermissionException {
        if (PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE)) this.wins = wins;
        else throw new NoPermissionException("No permission with plugin " + plugin.getName(), PluginPermission.PermissionType.KDWL_CHANGE);
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(JavaPlugin plugin, int losses) throws NoPermissionException {
        if (PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.KDWL_CHANGE)) this.losses = losses;
        else throw new NoPermissionException("No permission with plugin " + plugin.getName(), PluginPermission.PermissionType.KDWL_CHANGE);
    }

    public int getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(JavaPlugin plugin, int dailyProgress) throws NoPermissionException {
        if (PluginPermission.hasPermission(plugin, PluginPermission.PermissionType.DAILY_CHANGE))
            this.dailyProgress = dailyProgress;
        else throw new NoPermissionException("No permission with plugin " + plugin.getName(), PluginPermission.PermissionType.DAILY_CHANGE);
    }

    public String getName() {
        return name;
    }

    public void setName(JavaPlugin plugin, String name) {
        this.name = name;
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
