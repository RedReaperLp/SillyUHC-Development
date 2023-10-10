package com.github.redreaperlp.psa.database;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerTracker {
    private HashMap<UUID, PlayerData> playerDataHashMap = new HashMap<>();
    private PlayerStatsAPI playerStatsAPI;

    public PlayerTracker(PlayerStatsAPI playerStatsAPI) {
        this.playerStatsAPI = playerStatsAPI;
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataHashMap.getOrDefault(player.getUniqueId(), null);
    }

    public void loadPlayer(Player player) {
        if (playerDataHashMap.containsKey(player.getUniqueId())) {
            return;
        }
        PlayerData data = playerStatsAPI.getDatabase().getPlayerData(player);
        playerDataHashMap.put(player.getUniqueId(), data);
    }

    public void loadOnlinePlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        playerStatsAPI.getDatabase().getPlayerData(players).forEach(playerData -> playerDataHashMap.put(playerData.getUuid(), playerData));
    }

    public void savePlayer(Player player) {
        if (playerDataHashMap.containsKey(player.getUniqueId())) {
            playerStatsAPI.getDatabase().savePlayerData(playerDataHashMap.get(player.getUniqueId()));
            playerDataHashMap.remove(player.getUniqueId());
        }
    }

    protected void saveAll() {
        for (PlayerData data : playerDataHashMap.values()) {
            playerStatsAPI.getDatabase().savePlayerData(data);
        }
    }
}
