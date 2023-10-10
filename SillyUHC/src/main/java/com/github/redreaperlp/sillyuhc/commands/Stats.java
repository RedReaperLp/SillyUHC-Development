package com.github.redreaperlp.sillyuhc.commands;

import com.github.redreaperlp.psa.database.PlayerData;
import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Stats implements CommandTabCompleter {
    private SillyUHC sillyUHC;

    public Stats(SillyUHC sillyUHC) {
        this.sillyUHC = sillyUHC;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            PlayerData pd = sillyUHC.getPSA().getPlayerTracker().getPlayerData(player);
            if (pd == null) {
                AdventureUtil.sendWithPrefix(Component.text("You do not have any stats!", TextColor.color(0xff0000)), player);
                return true;
            }
            AdventureUtil.sendWithPrefix(Component.text("Your stats:", TextColor.color(0xff8c00)), player);
            AdventureUtil.sendWithPrefix(Component.text("Kills: " + pd.getKills(), TextColor.color(0xff8c00)), player);
            AdventureUtil.sendWithPrefix(Component.text("Deaths: " + pd.getDeaths(), TextColor.color(0xff8c00)), player);
            AdventureUtil.sendWithPrefix(Component.text("K/D: " + (pd.getDeaths() != 0 ? (pd.getKills() / pd.getDeaths()) : "----"), TextColor.color(0xff8c00)), player);
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
