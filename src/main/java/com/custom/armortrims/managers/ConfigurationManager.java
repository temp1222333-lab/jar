package com.custom.armortrims.managers;

import com.custom.armortrims.CustomArmorTrims;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    private final CustomArmorTrims plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
    private File dataFolder;

    public ConfigurationManager(CustomArmorTrims plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
    }

    public void loadConfigurations() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        // Load config.yml
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // Load messages.yml
        File messagesFile = new File(dataFolder, "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadConfigurations() {
        loadConfigurations();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    // Config getters
    public String getDatabaseType() {
        return config.getString("database.type", "sqlite");
    }

    public String getSqlitePath() {
        return config.getString("database.sqlite.file", "plugins/CustomArmorTrims/data.db");
    }

    public String getMysqlHost() {
        return config.getString("database.mysql.host", "localhost");
    }

    public int getMysqlPort() {
        return config.getInt("database.mysql.port", 3306);
    }

    public String getMysqlDatabase() {
        return config.getString("database.mysql.database", "customarmortrims");
    }

    public String getMysqlUsername() {
        return config.getString("database.mysql.username", "root");
    }

    public String getMysqlPassword() {
        return config.getString("database.mysql.password", "password");
    }

    public int getMysqlMaxPoolSize() {
        return config.getInt("database.mysql.max-pool-size", 10);
    }

    public long getMysqlConnectionTimeout() {
        return config.getLong("database.mysql.connection-timeout", 30000);
    }

    public long getMysqlIdleTimeout() {
        return config.getLong("database.mysql.idle-timeout", 600000);
    }

    public String getGuiTitle() {
        return config.getString("gui.title", "§6Armor Trims Customization");
    }

    public int getGuiSize() {
        return config.getInt("gui.size", 27);
    }

    public boolean isAutoApplyEnabled() {
        return config.getBoolean("gui.auto-apply", true);
    }

    public boolean isShowLockedPatterns() {
        return config.getBoolean("gui.show-locked-patterns", true);
    }

    public boolean isShowLockedMaterials() {
        return config.getBoolean("gui.show-locked-materials", true);
    }

    public boolean isSoundsEnabled() {
        return config.getBoolean("sounds.enable", true);
    }

    public String getTrimSelectedSound() {
        return config.getString("sounds.trim-selected", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getTrimAppliedSound() {
        return config.getString("sounds.trim-applied", "ITEM_ARMOR_EQUIP_GENERIC");
    }

    public String getGuiOpenSound() {
        return config.getString("sounds.gui-open", "BLOCK_CHEST_OPEN");
    }

    public boolean isAsyncDatabaseEnabled() {
        return config.getBoolean("performance.async-database", true);
    }

    public boolean isBatchUpdatesEnabled() {
        return config.getBoolean("performance.batch-updates", true);
    }

    public int getBatchSize() {
        return config.getInt("performance.batch-size", 10);
    }

    public boolean isCachePlayerDataEnabled() {
        return config.getBoolean("performance.cache-player-data", true);
    }

    public int getCacheTtlMinutes() {
        return config.getInt("performance.cache-ttl-minutes", 30);
    }

    public boolean isPlaceholderApiEnabled() {
        return config.getBoolean("placeholder-api.enabled", true);
    }

    // Message getters
    public String getPrefix() {
        return messages.getString("prefix", "§6[ArmorTrims] §r");
    }

    public String getMessage(String path) {
        return messages.getString(path, "Message not found: " + path);
    }

    public String getMessage(String path, Map<String, String> placeholders) {
        String message = getMessage(path);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }

    public String getArmorPieceName(String piece) {
        return messages.getString("armor-pieces." + piece, piece);
    }

    public String getPatternName(String pattern) {
        return messages.getString("patterns." + pattern, pattern);
    }

    public String getMaterialName(String material) {
        return messages.getString("materials." + material, material);
    }
}