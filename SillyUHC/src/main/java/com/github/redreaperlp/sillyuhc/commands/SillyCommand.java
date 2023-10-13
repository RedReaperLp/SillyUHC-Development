package com.github.redreaperlp.sillyuhc.commands;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.game.Game;
import com.github.redreaperlp.sillyuhc.NameSpacedKeyWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
                    if (!player.hasPermission("sillyuhc.start")) {
                        adventureUtil.sendWithPrefix(Component.text("You do not have permission to start the game!", TextColor.color(0xff0000)), player);
                        return true;
                    }
                    if (sillyUHC.getCurrentGame().currentPhaseType() != Game.PhaseType.WAITING) {
                        adventureUtil.sendWithPrefix(Component.text("The game is already running!", TextColor.color(0xff0000)), player);
                        return true;
                    }
                    sillyUHC.getCurrentGame().start();
                    adventureUtil.sendWithPrefix(Component.text("The game has been started!", TextColor.color(0x00ff00)), player);
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("spectate")) {
                if (!player.hasPermission("sillyuhc.spectate")) {
                    adventureUtil.sendWithPrefix(Component.text("You do not have permission to spectate!", TextColor.color(0xff0000)), player);
                }
                PersistentDataContainer pdc = player.getPersistentDataContainer();
                boolean spectate;
                if (args.length > 1) {
                    spectate = args[1].equalsIgnoreCase("yes");
                } else {
                    spectate = !pdc.get(NameSpacedKeyWrapper.keyParticipator, PersistentDataType.STRING).equals("spectating");
                }
                pdc.set(NameSpacedKeyWrapper.keyParticipator, PersistentDataType.STRING, spectate ? "spectating" : "participating");
                adventureUtil.sendWithPrefix(Component.text("You are now ", TextColor.color(0x00ff00))
                        .append(Component.text((spectate ? "spectating" : "participating"), TextColor.color(0xffff00)))
                        .append(Component.text("!", TextColor.color(0x00ff00))), player);
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
            List<String> valids = List.of("start", "map", "spectate");
            return valids.stream().filter(string -> string.startsWith(args[0])).toList();
        }
        return List.of();
    }
}
