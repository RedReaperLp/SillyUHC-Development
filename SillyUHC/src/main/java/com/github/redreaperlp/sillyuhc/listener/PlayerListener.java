package com.github.redreaperlp.sillyuhc.listener;

import com.github.redreaperlp.psa.database.PlayerData;
import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import com.github.redreaperlp.sillyuhc.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final SillyUHC sillyUHC;

    public PlayerListener(SillyUHC sillyUHC) {
        this.sillyUHC = sillyUHC;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(sillyUHC, ScoreboardManager::update, 1);
        PlayerData data = sillyUHC.getPSA().getPlayerTracker().getPlayerData(event.getPlayer());
        try {
            data.setCoins(sillyUHC, data.getCoins() + 5);
            data.setKills(sillyUHC, data.getKills() + 1);
        } catch (PlayerData.NoPermissionException e) {
            AdventureUtil.sendWithPrefix(Component.text("No permission! (" + e.getType().toString() + ") This is required for the Plugin to work properly!"), Bukkit.getServer().getConsoleSender());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(sillyUHC, ScoreboardManager::update, 1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            //TODO: Coin system
            AdventureUtil.broadcast(Component.text(player.getName() + " was killed by " + killer.getName() + "!"));
        } else {
            AdventureUtil.broadcast(Component.text(player.getName() + " died!"));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player player) {
            if (player.getInventory().getItemInMainHand().getType().toString().contains("axe")) {
                double od = event.getDamage();
                double nd = od * 0.5;
                event.setDamage(nd);
            }
            event.getFinalDamage();
        }

    }
}
