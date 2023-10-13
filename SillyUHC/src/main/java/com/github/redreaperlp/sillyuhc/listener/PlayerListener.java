package com.github.redreaperlp.sillyuhc.listener;

import com.github.redreaperlp.psa.database.PlayerData;
import com.github.redreaperlp.sillyuhc.NameSpacedKeyWrapper;
import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.game.Game;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import com.github.redreaperlp.utils.AdventureUtil;
import com.github.redreaperlp.utils.parser.StringParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
        Player player = event.getPlayer();
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (!pdc.has(NameSpacedKeyWrapper.keyParticipator, PersistentDataType.STRING)) {
            pdc.set(NameSpacedKeyWrapper.keyParticipator, PersistentDataType.STRING, "participating");
        }
        ScoreboardManager.addPlayer(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(sillyUHC, ScoreboardManager::update, 1);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ScoreboardManager.removePlayer(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(sillyUHC, ScoreboardManager::update, 1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Game game = sillyUHC.getCurrentGame();
        if (game.currentPhaseType().equals(Game.PhaseType.WAITING)) {
            event.setCancelled(true);
            return;
        }
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
            data.setKills(sillyUHC, data.getKills() + 1);

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
        Game game = sillyUHC.getCurrentGame();
        if (game.currentPhaseType().equals(Game.PhaseType.WAITING)) {
            event.setCancelled(true);
            return;
        }
        Entity damager = event.getDamager();
        if (damager instanceof Player player) {
            if (checkIfNoPvP(event, player)) return;
            if (player.getInventory().getItemInMainHand().getType().toString().contains("axe")) {
                int maxAxeDamage = 5;
                double od = event.getFinalDamage();
                double nd = (od > maxAxeDamage) ? maxAxeDamage : (od * 0.5);
                event.setDamage(nd);
            }
        } else if (damager instanceof Projectile projectile) {
            if (!(projectile.getShooter() instanceof Player player)) return;
            if (checkIfNoPvP(event, player)) return;
            if (player.getInventory().getItemInMainHand().getType().toString().contains("bow")) {
                int maxBowDamage = 1;
                double obd = event.getFinalDamage();
                double nbd = (obd > maxBowDamage) ? maxBowDamage : (obd * 0.5);
                event.setDamage(nbd);

            }
        }
    }

    @EventHandler
    public void onFluidPlace(PlayerBucketEmptyEvent event) {
        Game game = sillyUHC.getCurrentGame();
        if (game.currentPhaseType().equals(Game.PhaseType.WAITING)) {
            if (!event.getPlayer().hasPermission("sillyuhc.build")) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getBucket().equals(Material.LAVA_BUCKET)) {
            if (checkIfNoPvP(event, event.getPlayer())) ;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Game game = sillyUHC.getCurrentGame();
        if (game.currentPhaseType().equals(Game.PhaseType.WAITING)) {
            if (!event.getPlayer().hasPermission("sillyuhc.build")) {
                event.setCancelled(true);
                return;
            }
        }
        if (checkIfNoPvP(event)) ;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Game game = sillyUHC.getCurrentGame();
        if (game.currentPhaseType().equals(Game.PhaseType.WAITING)) {
            if (!event.getPlayer().hasPermission("sillyuhc.build")) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Checks if the player is allowed to damage other players in the current phase
     *
     * @param event  The event
     * @param player The player
     * @return true if the player is not allowed to damage other players, false otherwise
     */
    private boolean checkIfNoPvP(EntityDamageByEntityEvent event, Player player) {
        Game game = sillyUHC.getCurrentGame();
        if (game.getCurrentPhase().getPhaseType() == Game.PhaseType.NO_PVP) {
            if (!player.hasPermission("sillyuhc.nopvp.damage")) {
                adventureUtil.sendWithPrefix(Component.text("You can't damage players in this phase!", TextColor.color(0xff0000)), player);
                event.setCancelled(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player is allowed to place fluids that can damage other players in the current phase
     *
     * @param event  The event
     * @param player The player
     * @return true if the player is not allowed to place fluids that can damage other players, false otherwise
     */
    private boolean checkIfNoPvP(PlayerBucketEmptyEvent event, Player player) {
        Game game = sillyUHC.getCurrentGame();
        if (game.getCurrentPhase().getPhaseType() == Game.PhaseType.NO_PVP) {
            if (!player.hasPermission("sillyuhc.nopvp.place")) {
                adventureUtil.sendWithPrefix(Component.text("You can't place blocks that can damage players in this phase!", TextColor.color(0xff0000)), player);
                event.setCancelled(true);
                return true;
            }
        }
        return false;
    }

    List<Material> vulnerableBlocks = List.of(Material.LAVA, Material.FIRE,
            Material.CACTUS, Material.MAGMA_BLOCK, Material.CAMPFIRE, Material.SOUL_CAMPFIRE,
            Material.SWEET_BERRY_BUSH);

    /**
     * Checks if the player is allowed to place blocks that can damage other players in the current phase
     *
     * @param event The event
     * @return true if the player is not allowed to place blocks that can damage other players, false otherwise
     */
    private boolean checkIfNoPvP(BlockPlaceEvent event) {
        Game game = sillyUHC.getCurrentGame();
        if (game.getCurrentPhase().getPhaseType() == Game.PhaseType.NO_PVP) {
            if (!event.getPlayer().hasPermission("sillyuhc.nopvp.place")) {
                if (vulnerableBlocks.contains(event.getBlockPlaced().getType())) {
                    adventureUtil.sendWithPrefix(Component.text("You can't place blocks that can damage players in this phase!", TextColor.color(0xff0000)), event.getPlayer());
                    event.setCancelled(true);
                    return true;
                }
            }
        }
        return false;
    }
}
