package com.github.redreaperlp.sillyuhc;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import com.github.redreaperlp.sillyuhc.commands.CommandTabCompleter;
import com.github.redreaperlp.sillyuhc.commands.SillyCommand;
import com.github.redreaperlp.sillyuhc.commands.Stats;
import com.github.redreaperlp.sillyuhc.game.Game;
import com.github.redreaperlp.sillyuhc.game.Phase;
import com.github.redreaperlp.sillyuhc.listener.PlayerListener;
import com.github.redreaperlp.sillyuhc.ui.scoreboard.ScoreboardManager;
import com.github.redreaperlp.utils.AdventureUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class SillyUHC extends JavaPlugin {
    private static SillyUHC instance;
    public static Component prefix = Component.text("UHC » ", TextColor.color(0xff8c00), TextDecoration.BOLD);
    private PlayerStatsAPI api = (PlayerStatsAPI) Bukkit.getServer().getPluginManager().getPlugin("PlayerStatsAPI");
    public static AdventureUtil adventureUtil;
    public static VotedMap votedMap;
    private Game game;

    @Override
    public void onEnable() {
        instance = this;
        adventureUtil = new AdventureUtil(prefix);
        if (api != null) {
            if (api.isEnabled()) {
                adventureUtil.sendWithPrefix(Component.text("PlayerStatsAPI found!", TextColor.color(0x00ff00)), Bukkit.getConsoleSender());
            } else {
                adventureUtil.sendWithPrefix(Component.text("PlayerStatsAPI found but not enabled!", TextColor.color(0xff0000)), Bukkit.getConsoleSender());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } else {
            adventureUtil.sendWithPrefix(Component.text("PlayerStatsAPI not found!", TextColor.color(0xff0000)), Bukkit.getConsoleSender());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        loadSettings();

        registerCommand("sillyuhc", new SillyCommand(this));
        registerCommand("stats", new Stats(this));
        registerListeners();
        Bukkit.getOnlinePlayers().forEach(ScoreboardManager::addPlayer);
        ScoreboardManager.startUpdateThread();
        game = new Game(List.of(new Phase(Game.PhaseType.NO_PVP, 20), new Phase(Game.PhaseType.PVP, 20)), this);
        ticker = new Thread(() -> {
            while (!ticker.isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    game.tick();
                    synchronized (ScoreboardManager.update) {
                        ScoreboardManager.update.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        ticker.setName("SillyUHC-Ticker");
        ticker.start();
        String[] map = getConfig().getStringList("game-maps").get(0).split(":");
        votedMap = new VotedMap(Bukkit.createWorld(new WorldCreator(map[0])), Integer.parseInt(map[1]));
    }

    @Override
    public void onDisable() {
        if (ticker != null) ticker.interrupt();
        ScoreboardManager.stopUpdateThread();
    }

    public void loadSettings() {
        File outFile = new File(getDataFolder(), "config.yml");
        if (!outFile.exists()) {
            saveResource("config.yml", false);
        }
    }


    public static SillyUHC getInstance() {
        return instance;
    }


    public void registerCommand(String name, CommandTabCompleter executor) {
        getCommand(name).setExecutor(executor);
        getCommand(name).setTabCompleter(executor);
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public PlayerStatsAPI getPSA() {
        return api;
    }

    public Game getCurrentGame() {
        return game;
    }

    Thread ticker;






    //TODO: Teams/Solo <- Votable only if there is no "Host"
    //TODO: Map Voting
    //TODO: Nerfing [cross]- bow
    //TODO: Scoreboard (One for Lobby, one for Ingame) make it so you can have public and private scoreboards
}