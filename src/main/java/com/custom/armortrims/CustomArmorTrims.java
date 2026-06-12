package com.custom.armortrims;

import com.custom.armortrims.commands.ArmorTrimsCommand;
import com.custom.armortrims.database.DatabaseManager;
import com.custom.armortrims.listeners.ArmorEquipListener;
import com.custom.armortrims.managers.ConfigurationManager;
import com.custom.armortrims.managers.PlayerDataManager;
import com.custom.armortrims.managers.GuiManager;
import com.custom.armortrims.placeholder.ArmorTrimsPlaceholder;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Level;

public class CustomArmorTrims extends JavaPlugin {

    private static CustomArmorTrims instance;
    private DatabaseManager databaseManager;
    private ConfigurationManager configManager;
    private PlayerDataManager playerDataManager;
    private GuiManager guiManager;
    private BukkitScheduler scheduler;
    private AsyncScheduler asyncScheduler;

    @Override
    public void onEnable() {
        instance = this;
        scheduler = Bukkit.getScheduler();

        getLogger().info("═══════════════════════════════════════");
        getLogger().info("  CustomArmorTrims v" + getDescription().getVersion());
        getLogger().info("  Loading plugin...");
        getLogger().info("═══════════════════════════════════════");

        try {
            // Initialize configuration
            configManager = new ConfigurationManager(this);
            configManager.loadConfigurations();
            getLogger().info("✓ Configuration loaded");

            // Initialize database
            databaseManager = new DatabaseManager(this, configManager);
            databaseManager.initialize();
            getLogger().info("✓ Database initialized");

            // Initialize managers
            playerDataManager = new PlayerDataManager(this, databaseManager);
            guiManager = new GuiManager(this, configManager, playerDataManager);
            getLogger().info("✓ Managers initialized");

            // Register listeners
            registerListeners();
            getLogger().info("✓ Listeners registered");

            // Register commands
            registerCommands();
            getLogger().info("✓ Commands registered");

            // Register PlaceholderAPI hook
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new ArmorTrimsPlaceholder(this).register();
                getLogger().info("✓ PlaceholderAPI hook registered");
            }

            // Get async scheduler if available (Folia)
            try {
                asyncScheduler = Bukkit.getAsyncScheduler();
                getLogger().info("✓ Folia async scheduler detected");
            } catch (Exception e) {
                getLogger().info("✓ Using Paper async scheduler");
            }

            getLogger().info("═══════════════════════════════════════");
            getLogger().info("✓ Plugin enabled successfully!");
            getLogger().info("═══════════════════════════════════════");

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable plugin", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("═══════════════════════════════════════");
        getLogger().info("Disabling CustomArmorTrims...");

        if (databaseManager != null) {
            databaseManager.shutdown();
            getLogger().info("✓ Database connection closed");
        }

        getLogger().info("✓ Plugin disabled");
        getLogger().info("═══════════════════════════════════════");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(this, playerDataManager, guiManager), this);
    }

    private void registerCommands() {
        new ArmorTrimsCommand(this, configManager, playerDataManager, guiManager).register();
    }

    public static CustomArmorTrims getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    public AsyncScheduler getAsyncScheduler() {
        return asyncScheduler;
    }

    public void log(String message) {
        getLogger().info(message);
    }

    public void logError(String message, Exception e) {
        getLogger().log(Level.SEVERE, message, e);
    }
}