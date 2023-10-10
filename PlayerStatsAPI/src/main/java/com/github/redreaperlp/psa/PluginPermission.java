package com.github.redreaperlp.psa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static com.github.redreaperlp.psa.PlayerStatsAPI.adventureUtil;

public record PluginPermission(Plugin plugin, boolean coinChangePerm, boolean kdwlChangePerm, boolean dailyChangePerm, boolean databaseAccessPerm) {
    private static final List<PluginPermission> allowedPlugins = new ArrayList<>();

    public static void addAllowedPlugin(Plugin plugin) {
        if (plugin == null) return;
        FileConfiguration conf = PlayerStatsAPI.getInstance().getConfig();
        if (!contains(plugin)) {
            if (plugin.equals(PlayerStatsAPI.getInstance())) {
                allowedPlugins.add(new PluginPermission(PlayerStatsAPI.getInstance(), true, true, true, true));
            }
            boolean databaseAccess = conf.getStringList("plugin.full-database-access").contains(plugin.getName());
            if (conf.getStringList("plugin.full-access").contains(plugin.getName())) {
                adventureUtil.sendWithPrefix(Component.text("Adding plugin " + plugin.getName() + " to allowed plugins", TextColor.color(0x00ff00)), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
                allowedPlugins.add(new PluginPermission(plugin, true, true, true, databaseAccess));
            } else {
                boolean coinChange = conf.getStringList("plugin.coin-change").contains(plugin.getName());
                boolean kdwlChange = conf.getStringList("plugin.kdwl-change").contains(plugin.getName());
                boolean dailyChange = conf.getStringList("plugin.daily-change").contains(plugin.getName());
                adventureUtil.sendWithPrefix(Component.text("Adding plugin " + plugin.getName() + " to allowed plugins with: ", TextColor.color(0x00ff00))
                        .append(Component.text("coinChange: " + coinChange + ", ", TextColor.color(0xffff00)))
                        .append(Component.text("kdwlChange: " + kdwlChange + ", ", TextColor.color(0xffff00)))
                        .append(Component.text("dailyChange: " + dailyChange, TextColor.color(0xffff00))), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
                if (!coinChange && !kdwlChange && !dailyChange) return;
                allowedPlugins.add(new PluginPermission(plugin,
                        coinChange,
                        kdwlChange,
                        dailyChange,
                        databaseAccess));
            }
        }
    }

    private static PluginPermission getPluginPermission(Plugin plugin) {
        for (PluginPermission permission : allowedPlugins) {
            if (permission.plugin().equals(plugin)) {
                return permission;
            }
            return permission;
        }
        return null;
    }

    public static boolean contains(Plugin plugin) {
        return getPluginPermission(plugin) != null;
    }

    public static boolean hasPermission(Plugin plugin, PermissionType type) {
        PluginPermission perm = getPluginPermission(plugin);
        if (perm == null) return false;
        if (!perm.callerIsPlugin(plugin)) return false;
        switch (type) {
            case COIN_CHANGE -> {
                return perm.coinChangePerm();
            }
            case KDWL_CHANGE -> {
                return perm.kdwlChangePerm();
            }
            case DAILY_CHANGE -> {
                return perm.dailyChangePerm();
            }
        }
        return false;
    }

    public boolean callerIsPlugin(Plugin plugin) {
        String fileName = plugin.getClass().getClassLoader().getName();
        String lastNotNull = "";
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTrace = thread.getStackTrace();

        for (StackTraceElement element : stackTrace) {
            String classLoaderName = element.getClassLoaderName();
            if (classLoaderName == null) continue;
            lastNotNull = classLoaderName;
        }
        if (lastNotNull.equals(fileName)) return true;
        adventureUtil.sendWithPrefix(Component.text("Plugin with Filename ", TextColor.color(0xff0000))
                .append(Component.text(lastNotNull, TextColor.color(0xffff00)))
                .append(Component.text(" is trying to access PlayerStatsAPI with plugin ", TextColor.color(0xff0000)))
                .append(Component.text(plugin.getName(), TextColor.color(0xffff00)))
                .append(Component.text(" (Filename: ", TextColor.color(0xff0000)))
                .append(Component.text(fileName, TextColor.color(0xffff00)))
                .append(Component.text(")!", TextColor.color(0xff0000))), Bukkit.getServer().getConsoleSender());
        return false;
    }

    public enum PermissionType {
        COIN_CHANGE,
        KDWL_CHANGE,
        DAILY_CHANGE,
        DATABASE_ACCESS
    }
}
