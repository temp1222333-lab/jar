package com.custom.armortrims.gui;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.managers.GuiManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PatternSelectionGui {
    private final CustomArmorTrims plugin;
    private final GuiManager guiManager;
    private final Player player;
    private final String armorPiece;
    private Inventory inventory;

    private static final String[] PATTERNS = {
            "sentry", "vex", "wild", "coast", "dune", "wayfinder",
            "shaper", "silence", "ward", "tide", "raiser", "host"
    };

    public PatternSelectionGui(CustomArmorTrims plugin, GuiManager guiManager, Player player, String armorPiece) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.player = player;
        this.armorPiece = armorPiece;
    }

    public void open() {
        inventory = plugin.getServer().createInventory(null, 36, plugin.getConfigManager().getMessage("gui.pattern-title"));
        setupItems();
        player.openInventory(inventory);
    }

    private void setupItems() {
        int slot = 0;
        for (String pattern : PATTERNS) {
            String permission = "armortrims.pattern." + pattern;
            boolean hasPermission = player.hasPermission(permission) || player.hasPermission("armortrims.admin");

            Material material = hasPermission ? Material.DIAMOND : Material.BARRIER;
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.displayName(Component.text(
                        (hasPermission ? "§a" : "§c") +
                        plugin.getConfigManager().getMessage("patterns." + pattern)
                ));
                item.setItemMeta(meta);
            }

            inventory.setItem(slot++, item);
        }

        // Back button
        ItemStack backItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta backMeta = backItem.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("§7Back"));
            backItem.setItemMeta(backMeta);
        }
        inventory.setItem(34, backItem);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();

        if (slot == 34) {
            guiManager.openMainGui(player);
            return;
        }

        if (slot < PATTERNS.length) {
            String pattern = PATTERNS[slot];
            String permission = "armortrims.pattern." + pattern;

            if (!player.hasPermission(permission) && !player.hasPermission("armortrims.admin")) {
                player.sendMessage(plugin.getConfigManager().getPrefix() +
                        plugin.getConfigManager().getMessage("permissions.pattern-locked"));
                return;
            }

            // Open material selection
            guiManager.openMaterialGui(player, armorPiece);
        }
    }
}