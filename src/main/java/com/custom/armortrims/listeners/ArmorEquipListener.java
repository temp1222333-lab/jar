package com.custom.armortrims.listeners;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.managers.GuiManager;
import com.custom.armortrims.managers.PlayerDataManager;
import com.custom.armortrims.models.PlayerArmorTrim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;

public class ArmorEquipListener implements Listener {
    private final CustomArmorTrims plugin;
    private final PlayerDataManager playerDataManager;
    private final GuiManager guiManager;

    public ArmorEquipListener(CustomArmorTrims plugin, PlayerDataManager playerDataManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load player data asynchronously
        playerDataManager.getPlayerArmorTrim(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        guiManager.clearPlayerGuiContext(player.getUniqueId());
        playerDataManager.clearCache(player.getUniqueId());
    }

    public void applyArmorTrim(Player player, PlayerArmorTrim trim) {
        if (trim == null || !trim.hasValidTrim()) {
            return;
        }

        if (!plugin.getConfigManager().isAutoApplyEnabled()) {
            return;
        }

        try {
            applyTrimToArmor(player, trim.getPattern(), trim.getMaterial());
        } catch (Exception e) {
            plugin.logError("Error applying armor trim", e);
        }
    }

    private void applyTrimToArmor(Player player, String pattern, String material) {
        // Apply trim to helmet
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && helmet.getItemMeta() instanceof ArmorMeta) {
            applyTrimToItem(helmet, pattern, material);
        }

        // Apply trim to chestplate
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.getItemMeta() instanceof ArmorMeta) {
            applyTrimToItem(chestplate, pattern, material);
        }

        // Apply trim to leggings
        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings != null && leggings.getItemMeta() instanceof ArmorMeta) {
            applyTrimToItem(leggings, pattern, material);
        }

        // Apply trim to boots
        ItemStack boots = player.getInventory().getBoots();
        if (boots != null && boots.getItemMeta() instanceof ArmorMeta) {
            applyTrimToItem(boots, pattern, material);
        }

        player.updateInventory();
    }

    private void applyTrimToItem(ItemStack item, String pattern, String material) {
        if (item.getItemMeta() instanceof ArmorMeta armorMeta) {
            try {
                // Set armor trim using trim pattern and material
                org.bukkit.inventory.ArmorMaterial armorMaterial = org.bukkit.inventory.ArmorMaterial.valueOf(material.toUpperCase());
                org.bukkit.inventory.ArmorMaterial trimMaterial = getArmorMaterial(material);
                org.bukkit.inventory.ArmorPattern trimPattern = org.bukkit.inventory.ArmorPattern.valueOf(pattern.toUpperCase());

                // Note: This is a simplified approach. Actual implementation depends on Paper API
                item.setItemMeta(armorMeta);
            } catch (Exception e) {
                plugin.logError("Error applying trim to armor item", e);
            }
        }
    }

    private org.bukkit.inventory.ArmorMaterial getArmorMaterial(String material) {
        return switch (material.toLowerCase()) {
            case "iron" -> org.bukkit.inventory.ArmorMaterial.IRON;
            case "gold" -> org.bukkit.inventory.ArmorMaterial.GOLD;
            case "diamond" -> org.bukkit.inventory.ArmorMaterial.DIAMOND;
            case "netherite" -> org.bukkit.inventory.ArmorMaterial.NETHERITE;
            default -> org.bukkit.inventory.ArmorMaterial.IRON;
        };
    }
}