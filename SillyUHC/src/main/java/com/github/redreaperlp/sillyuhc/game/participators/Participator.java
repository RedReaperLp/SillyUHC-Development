package com.github.redreaperlp.sillyuhc.game.participators;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Participator {
    private UUID playerUUID;
    private String playerName;
    private List<Participator> killedBy = new ArrayList<>();

    public Participator(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();

        updateStats(player);
    }

    private void updateStats(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExhaustion(0);
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setRemainingAir(player.getMaximumAir());
        player.setNoDamageTicks(0);
        player.setLastDamageCause(null);
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
