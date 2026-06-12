package com.custom.armortrims.commands;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.managers.ConfigurationManager;
import com.custom.armortrims.managers.GuiManager;
import com.custom.armortrims.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ArmorTrimsCommand implements CommandExecutor {
    private final CustomArmorTrims plugin;
    private final ConfigurationManager configManager;
    private final PlayerDataManager playerDataManager;
    private final GuiManager guiManager;

    public ArmorTrimsCommand(CustomArmorTrims plugin, ConfigurationManager configManager,
                             PlayerDataManager playerDataManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerDataManager = playerDataManager;
        this.guiManager = guiManager;
    }

    public void register() {
        plugin.getCommand("armortrims").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("armortrims.use")) {
            player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.no-permission"));
            return true;
        }

        if (args.length == 0) {
            // Open main GUI
            guiManager.openMainGui(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "reload":
                handleReload(player);
                break;
            case "reset":
                handleReset(player);
                break;
            default:
                player.sendMessage(configManager.getPrefix() + "§cUnknown command. Usage: /armortrims [reload|reset]");
        }

        return true;
    }

    private void handleReload(Player player) {
        if (!player.hasPermission("armortrims.reload") && !player.hasPermission("armortrims.admin")) {
            player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.no-permission"));
            return;
        }

        try {
            configManager.reloadConfigurations();
            playerDataManager.clearAllCache();
            player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.reload-success"));
            plugin.log("Plugin configuration reloaded by " + player.getName());
        } catch (Exception e) {
            player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.reload-error"));
            plugin.logError("Error reloading configuration", e);
        }
    }

    private void handleReset(Player player) {
        if (!player.hasPermission("armortrims.reset")) {
            player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.no-permission"));
            return;
        }

        playerDataManager.resetPlayerArmorTrim(player.getUniqueId())
                .thenRun(() -> {
                    player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.reset-success"));
                })
                .exceptionally(e -> {
                    player.sendMessage(configManager.getPrefix() + configManager.getMessage("commands.reset-error"));
                    plugin.logError("Error resetting player armor trim", (Exception) e);
                    return null;
                });
    }
}