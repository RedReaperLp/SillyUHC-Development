package com.github.redreaperlp.psa.database;

import com.github.redreaperlp.psa.PlayerStatsAPI;
import com.github.redreaperlp.psa.util.AdventureUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Database {
    private HikariDataSource dataSource;

    public Database(String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
        try (Connection con = dataSource.getConnection()) {
            String useDatabaseQuery = "USE " + database + ";";
            PreparedStatement statement = con.prepareStatement(useDatabaseQuery);
            statement.execute();
            AdventureUtil.sendWithPrefix(Component.text("(Database | Connected Successfully)", TextColor.color(0x00ff00)), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
        } catch (Exception e) {
            handleOtherException(e, database);
        }
        initTables();
        AdventureUtil.sendWithPrefix(Component.text("(Database | Initialized Successfully)", TextColor.color(0x00ff00)), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
    }

    private void handleOtherException(Exception e, String database) {
        AdventureUtil.sendWithPrefix(Component.text("Error while connecting to database ", TextColor.color(0xff0000))
                .append(Component.text(database, TextColor.color(0x00ff00)))
                .append(Component.text("!", TextColor.color(0xff0000))), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
        AdventureUtil.sendWithPrefix(Component.text(e.getMessage(), TextColor.color(0xff0000)), PlayerStatsAPI.getInstance().getServer().getConsoleSender());
        PlayerStatsAPI.getInstance().getServer().getPluginManager().disablePlugin(PlayerStatsAPI.getInstance());
    }

    public void stop() {
        PlayerStatsAPI.getInstance().getPlayerTracker().saveAll();
        dataSource.close();
    }

    public void initTables() {
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `stats` (`UUID` varchar(36) NOT NULL PRIMARY KEY UNIQUE," +
                    "`NAME` varchar(16) NOT NULL," +
                    "`COINS` int(11) NOT NULL DEFAULT 0," +
                    "`KILLS` int(11) NOT NULL DEFAULT 0," +
                    "`DEATHS` int(11) NOT NULL DEFAULT 0," +
                    "`WINS` int(11) NOT NULL DEFAULT 0," +
                    "`LOSSES` int(11) NOT NULL DEFAULT 0," +
                    "`DAILY_PROGRESS` TINYINT NOT NULL DEFAULT 0);");
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected PlayerData getPlayerData(Player player) {
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM stats WHERE `UUID` = ?;");
            statement.setString(1, player.getUniqueId().toString());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return createFromRS(resultSet, player);
            } else {
                return new PlayerData(player, 0, 0, 0, 0, 0, 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<PlayerData> getPlayerData(List<Player> playerList) {
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM stats WHERE `UUID` = ?;");
            List<PlayerData> playerDataList = new ArrayList<>();
            for (Player player : playerList) {
                statement.setString(1, player.getUniqueId().toString());
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    playerDataList.add(createFromRS(resultSet, player));
                } else {
                    playerDataList.add(new PlayerData(player, 0, 0, 0, 0, 0, 0));
                }
            }
            return playerDataList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerData createFromRS(ResultSet resultSet, Player player) throws SQLException {
        int coins = resultSet.getInt("COINS");
        int kills = resultSet.getInt("KILLS");
        int deaths = resultSet.getInt("DEATHS");
        int wins = resultSet.getInt("WINS");
        int losses = resultSet.getInt("LOSSES");
        int dailyProgress = resultSet.getInt("DAILY_PROGRESS");
        return new PlayerData(player, coins, kills, deaths, wins, losses, dailyProgress);
    }

    protected void savePlayerData(PlayerData data) {
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement("INSERT INTO stats (`UUID`, `NAME`, `COINS`, `KILLS`, `DEATHS`, `WINS`, `LOSSES`, `DAILY_PROGRESS`) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE `NAME` = VALUES(`NAME`), `COINS` = VALUES(`COINS`), `KILLS` = VALUES(`KILLS`), `DEATHS` = VALUES(`DEATHS`), `WINS` = VALUES(`WINS`), `LOSSES` = VALUES(`LOSSES`), `DAILY_PROGRESS` = VALUES(`DAILY_PROGRESS`);");
            prepareStatement(data, statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void savePlayerData(HashMap<UUID, PlayerData> data) {
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement("INSERT INTO stats (`UUID`, `NAME`, `COINS`, `KILLS`, `DEATHS`, `WINS`, `LOSSES`, `DAILY_PROGRESS`) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE `NAME` = VALUES(`NAME`), `COINS` = VALUES(`COINS`), `KILLS` = VALUES(`KILLS`), `DEATHS` = VALUES(`DEATHS`), `WINS` = VALUES(`WINS`), `LOSSES` = VALUES(`LOSSES`), `DAILY_PROGRESS` = VALUES(`DAILY_PROGRESS`);");
            for (PlayerData playerData : data.values()) {
                prepareStatement(playerData, statement);
                statement.addBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareStatement(PlayerData data, PreparedStatement statement) throws SQLException {
        statement.setString(1, data.getUuid().toString());
        statement.setString(2, data.getName());
        statement.setInt(3, data.getCoins());
        statement.setInt(4, data.getKills());
        statement.setInt(5, data.getDeaths());
        statement.setInt(6, data.getWins());
        statement.setInt(7, data.getLosses());
        statement.setInt(8, data.getDailyProgress());
    }
}
