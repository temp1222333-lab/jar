package com.custom.armortrims.database;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.managers.ConfigurationManager;
import com.custom.armortrims.models.PlayerArmorTrim;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class DatabaseManager {
    private final CustomArmorTrims plugin;
    private final ConfigurationManager configManager;
    private HikariDataSource dataSource;
    private final Executor asyncExecutor;

    public DatabaseManager(CustomArmorTrims plugin, ConfigurationManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.asyncExecutor = ForkJoinPool.commonPool();
    }

    public void initialize() throws SQLException {
        String dbType = configManager.getDatabaseType();

        if ("mysql".equalsIgnoreCase(dbType)) {
            initializeMysql();
        } else {
            initializeSqlite();
        }

        createTables();
    }

    private void initializeMysql() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s",
                configManager.getMysqlHost(),
                configManager.getMysqlPort(),
                configManager.getMysqlDatabase()));
        config.setUsername(configManager.getMysqlUsername());
        config.setPassword(configManager.getMysqlPassword());
        config.setMaximumPoolSize(configManager.getMysqlMaxPoolSize());
        config.setConnectionTimeout(configManager.getMysqlConnectionTimeout());
        config.setIdleTimeout(configManager.getMysqlIdleTimeout());

        this.dataSource = new HikariDataSource(config);
        plugin.log("Connected to MySQL database");
    }

    private void initializeSqlite() throws SQLException {
        String sqlitePath = configManager.getSqlitePath();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + sqlitePath);
        config.setMaximumPoolSize(5);
        config.setAutoCommit(true);

        this.dataSource = new HikariDataSource(config);
        plugin.log("Connected to SQLite database: " + sqlitePath);
    }

    private void createTables() throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS player_armor_trims (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "pattern VARCHAR(255)," +
                "material VARCHAR(255)," +
                "last_updated BIGINT" +
                ")";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        }
        plugin.log("Database tables created/verified");
    }

    public CompletableFuture<PlayerArmorTrim> getPlayerArmorTrim(java.util.UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT uuid, pattern, material, last_updated FROM player_armor_trims WHERE uuid = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, playerUuid.toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new PlayerArmorTrim(
                                playerUuid,
                                rs.getString("pattern"),
                                rs.getString("material")
                        );
                    }
                }
            } catch (SQLException e) {
                plugin.logError("Error retrieving player armor trim", e);
            }
            return null;
        }, asyncExecutor);
    }

    public CompletableFuture<Void> savePlayerArmorTrim(PlayerArmorTrim trim) {
        return CompletableFuture.runAsync(() -> {
            String sql = "INSERT OR REPLACE INTO player_armor_trims (uuid, pattern, material, last_updated) VALUES (?, ?, ?, ?)";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, trim.getPlayerUuid().toString());
                stmt.setString(2, trim.getPattern());
                stmt.setString(3, trim.getMaterial());
                stmt.setLong(4, trim.getLastUpdated());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.logError("Error saving player armor trim", e);
            }
        }, asyncExecutor);
    }

    public CompletableFuture<Void> deletePlayerArmorTrim(java.util.UUID playerUuid) {
        return CompletableFuture.runAsync(() -> {
            String sql = "DELETE FROM player_armor_trims WHERE uuid = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, playerUuid.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.logError("Error deleting player armor trim", e);
            }
        }, asyncExecutor);
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.log("Database connection closed");
        }
    }
}