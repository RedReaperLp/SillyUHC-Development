package com.github.redreaperlp.psa.database;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerTracker {
    private HashMap<UUID, PlayerData> playerDataHashMap = new HashMap<>();
    private PlayerStatsAPI playerStatsAPI;
    private Database database;

    public PlayerTracker(PlayerStatsAPI playerStatsAPI) {
        this.playerStatsAPI = playerStatsAPI;
        database = playerStatsAPI.getDatabase(playerStatsAPI);
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataHashMap.getOrDefault(player.getUniqueId(), null);
    }

    public void loadPlayer(Player player) {
        if (playerDataHashMap.containsKey(player.getUniqueId())) {
            return;
        }
        PlayerData data = database.getPlayerData(player);
        playerDataHashMap.put(player.getUniqueId(), data);
    }

    public void loadOnlinePlayers() {
        List<PlayerData> data =  database.getPlayerData(new ArrayList<>(Bukkit.getOnlinePlayers()));
        data.forEach(playerData -> playerDataHashMap.put(playerData.getUuid(), playerData));
    }

    public void savePlayer(Player player) {
        if (playerDataHashMap.containsKey(player.getUniqueId())) {
            database.savePlayerData(playerDataHashMap.get(player.getUniqueId()));
            playerDataHashMap.remove(player.getUniqueId());
        }
    }

    protected void saveAll() {
        for (PlayerData data : playerDataHashMap.values()) {
            database.savePlayerData(data);
        }
    }
}
