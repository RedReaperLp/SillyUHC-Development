package com.github.redreaperlp.psa.listener;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    PlayerStatsAPI playerStatsAPI;
    public PlayerListener(PlayerStatsAPI playerStatsAPI) {
        this.playerStatsAPI = playerStatsAPI;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerStatsAPI.getInstance().getPlayerTracker().loadPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerStatsAPI.getInstance().getPlayerTracker().savePlayer(e.getPlayer());
    }
}
