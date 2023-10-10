package com.github.redreaperlp.psa;

import com.github.redreaperlp.psa.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginPermission {
    private static final List<PluginPermission> allowedPlugins = new ArrayList<>();

    private final Plugin plugin;
    private final boolean coinChangePerm;
    private final boolean kdwlChangePerm;
    private final boolean dailyChangePerm;

    public PluginPermission(Plugin plugin, boolean coinChangePerm, boolean kdwlChangePerm, boolean dailyChangePerm) {
        this.plugin = plugin;
        this.coinChangePerm = coinChangePerm;
        this.kdwlChangePerm = kdwlChangePerm;
        this.dailyChangePerm = dailyChangePerm;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public boolean isCoinChangePerm() {
        return coinChangePerm;
    }

    public boolean isKdwlChangePerm() {
        return kdwlChangePerm;
    }

    public boolean isDailyChangePerm() {
        return dailyChangePerm;
    }

    public static void addAllowedPlugin(Plugin plugin) {
        if (plugin == null) return;
        FileConfiguration conf = PlayerStatsAPI.getInstance().getConfig();
        if (!contains(plugin)) {
            if (conf.getStringList("plugin-full-access").contains(plugin.getName())) {
                AdventureUtil.sendWithPrefix(Component.text("Adding plugin " + plugin.getName() + " to allowed plugins", TextColor.color(0x00ff00)), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
                allowedPlugins.add(new PluginPermission(plugin, true, true, true));
            } else {
                boolean coinChange = conf.getStringList("plugin-coin-change").contains(plugin.getName());
                boolean kdwlChange = conf.getStringList("plugin-kdwl-change").contains(plugin.getName());
                boolean dailyChange = conf.getStringList("plugin-daily-change").contains(plugin.getName());
                AdventureUtil.sendWithPrefix(Component.text("Adding plugin " + plugin.getName() + " to allowed plugins with: ",TextColor.color(0x00ff00))
                        .append(Component.text("coinChange: " + coinChange + ", ", TextColor.color(0xffff00)))
                        .append(Component.text("kdwlChange: " + kdwlChange + ", ", TextColor.color(0xffff00)))
                        .append(Component.text("dailyChange: " + dailyChange, TextColor.color(0xffff00))), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
                if (!coinChange && !kdwlChange && !dailyChange) return;
                allowedPlugins.add(new PluginPermission(plugin,
                        coinChange,
                        kdwlChange,
                        dailyChange));
            }
        }
    }

    private static PluginPermission getPluginPermission(Plugin plugin) {
        for (PluginPermission permission : allowedPlugins) {
            if (permission.getPlugin().equals(plugin)) {
                return permission;
            }
        }
        return null;
    }

    public static boolean contains(Plugin plugin) {
        for (PluginPermission permission : allowedPlugins) {
            if (permission.getPlugin().equals(plugin)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermission(Plugin plugin, PermissionType type) {
        PluginPermission perm = getPluginPermission(plugin);
        if (perm == null) return false;
        switch (type) {
            case COIN_CHANGE -> {
                return perm.isCoinChangePerm();
            }
            case KDWL_CHANGE -> {
                return perm.isKdwlChangePerm();
            }
            case DAILY_CHANGE -> {
                return perm.isDailyChangePerm();
            }
        }
        return false;
    }

    public enum PermissionType {
        COIN_CHANGE,
        KDWL_CHANGE,
        DAILY_CHANGE
    }
}
