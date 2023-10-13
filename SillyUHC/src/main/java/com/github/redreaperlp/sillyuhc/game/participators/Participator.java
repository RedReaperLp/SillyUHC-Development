package com.github.redreaperlp.sillyuhc.game.participators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.redreaperlp.sillyuhc.SillyUHC.adventureUtil;

public class Participator {
    private UUID playerUUID;
    private String playerName;
    private List<Participator> killedBy = new ArrayList<>();

    public Participator(UUID playerUUID, String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;

        //TODO: update all stats like hunger, health, exp, etc.
        adventureUtil.sendWithPrefix(Component.text("You are Participating", TextColor.color(0xff0000)),Bukkit.getPlayer(playerUUID));
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void killed(Participator participator) {
        killedBy.add(participator);
    }

    public List<Participator> getKills() {
        return killedBy;
    }
}
