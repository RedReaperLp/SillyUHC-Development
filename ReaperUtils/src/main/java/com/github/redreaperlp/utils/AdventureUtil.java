package com.github.redreaperlp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class AdventureUtil {
    private Component prefix;

    public AdventureUtil(Component prefix) {
        this.prefix = prefix;
    }
    public void sendWithPrefix(Component message, CommandSender receiver) {
        if (message.color() == null) message = message.color(TextColor.color(0xffffff));
        send(receiver, prefix.append(message));
    }

    public void sendWithPrefix(Component message, Player... receivers) {
        for (Player receiver : receivers) {
            sendWithPrefix(message, receiver);
        }
    }

    public static void send(CommandSender receiver, Component message) {
        receiver.spigot().sendMessage(BungeeComponentSerializer.get().serialize(message));
    }

    public static void send(Component message, Player... receivers) {
        for (Player receiver : receivers) {
            send(receiver, message);
        }
    }

    public static void broadcast(Component message) {
        Bukkit.spigot().broadcast(serializeToBaseComponent(message));
    }

    public static String serialize(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static BaseComponent[] serializeToBaseComponent(Component component) {
        return BungeeComponentSerializer.get().serialize(component);
    }

    public static List<String> serialize(List<Component> components) {
        return components.stream().map(AdventureUtil::serialize).collect(Collectors.toList());
    }

    public static void sendPermissionMessage(Component message, String permission) {
        Bukkit.getOnlinePlayers().stream().filter(
                player -> player.hasPermission(permission)).forEach(player -> {
            send(player, message);
        });
    }
}
