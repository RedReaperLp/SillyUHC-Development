package com.github.redreaperlp.sillyuhc.commands;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.game.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.github.redreaperlp.sillyuhc.SillyUHC.adventureUtil;

public class SillyCommand implements CommandTabCompleter {
    private SillyUHC sillyUHC;
    public SillyCommand(SillyUHC sillyUHC) {
        this.sillyUHC = sillyUHC;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 0) {
                adventureUtil.sendWithPrefix(Component.text("SillyUHC v1.0", TextColor.color(0xff8c00)), player);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("start")) {
                    if (Game.startNewGame(sillyUHC)) {
                        adventureUtil.sendWithPrefix(Component.text("Started new game!", TextColor.color(0x00ff00)), player);
                    } else {
                        adventureUtil.sendWithPrefix(Component.text("There is already a game running!", TextColor.color(0xff0000)), player);
                    }
                }
            }
            return true;
        } else {
            adventureUtil.sendWithPrefix(Component.text("You may not execute this command as a non-player!", TextColor.color(0xff0000)), commandSender);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> valids = List.of("start");
            return valids.stream().filter(string -> string.startsWith(args[0])).toList();
        }
        return List.of();
    }
}
