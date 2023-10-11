package com.github.redreaperlp.sillyuhc.listener;

import com.github.redreaperlp.psa.database.PlayerData;
import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

import static com.github.redreaperlp.sillyuhc.SillyUHC.adventureUtil;

public class PlayerListener implements Listener {
    private final SillyUHC sillyUHC;

    public PlayerListener(SillyUHC sillyUHC) {
        this.sillyUHC = sillyUHC;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Bukkit.getScheduler().runTaskLater(sillyUHC, ScoreboardManager::update, 1);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(sillyUHC, ScoreboardManager::update, 1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.remove(player);

        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
            players.remove(killer);

            PlayerData data = sillyUHC.getPSA().getPlayerTracker().getPlayerData(killer);
            data.setCoins(sillyUHC, data.getCoins() + 10);

            adventureUtil.sendWithPrefix(Component.text(player.getName(), TextColor.color(0x0096FF))
                    .append(Component.text(" was killed by ", TextColor.color(0xff8c00)))
                    .append(Component.text(killer.getName(), TextColor.color(0xffff00))), players.toArray(Player[]::new));
            adventureUtil.sendWithPrefix(Component.text("You killed ", TextColor.color(0xff8c00))
                    .append(Component.text(player.getName(), TextColor.color(0x0096FF)))
                    .append(Component.text(" (10 Coins)", TextColor.color(0x00ff00))), killer);
            adventureUtil.sendWithPrefix(Component.text("You were killed by ", TextColor.color(0xff8c00))
                    .append(Component.text(killer.getName(), TextColor.color(0xffff00))), player);
        } else {
            adventureUtil.sendWithPrefix(Component.text(player.getName(), TextColor.color(0x0096FF))
                    .append(Component.text(" died!", TextColor.color(0xff8c00))), players.toArray(Player[]::new));
            adventureUtil.sendWithPrefix(Component.text("You died!", TextColor.color(0xff8c00)), player);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (damager instanceof Player player) {
            if (player.getInventory().getItemInMainHand().getType().toString().contains("axe")) {
                int maxAxeDamage = 5;

                double od = event.getFinalDamage();
                double nd = (od > maxAxeDamage) ? maxAxeDamage : (od * 0.5);
                event.setDamage(nd);

            } else if (player.getInventory().getItemInMainHand().getType().toString().contains("bow")) {
                int maxBowDamage = 1;

                double obd = event.getFinalDamage();
                double nbd = (obd > maxBowDamage) ? maxBowDamage : (obd * 0.5);
                event.setDamage(nbd);

            }
        }
    }
}
