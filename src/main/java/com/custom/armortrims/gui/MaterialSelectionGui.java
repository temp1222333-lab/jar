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

public class MaterialSelectionGui {
    private final CustomArmorTrims plugin;
    private final GuiManager guiManager;
    private final Player player;
    private final String armorPiece;
    private Inventory inventory;

    private static final String[] MATERIALS = {
            "iron", "gold", "diamond", "emerald", "redstone",
            "lapis", "quartz", "netherite", "amethyst", "copper"
    };

    private static final Material[] MATERIAL_ICONS = {
            Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND,
            Material.EMERALD, Material.REDSTONE, Material.LAPIS_LAZULI,
            Material.QUARTZ, Material.NETHERITE_INGOT, Material.AMETHYST_SHARD,
            Material.COPPER_INGOT
    };

    public MaterialSelectionGui(CustomArmorTrims plugin, GuiManager guiManager, Player player, String armorPiece) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.player = player;
        this.armorPiece = armorPiece;
    }

    public void open() {
        inventory = plugin.getServer().createInventory(null, 27, plugin.getConfigManager().getMessage("gui.material-title"));
        setupItems();
        player.openInventory(inventory);
    }

    private void setupItems() {
        int slot = 0;
        for (int i = 0; i < MATERIALS.length; i++) {
            String material = MATERIALS[i];
            String permission = "armortrims.material." + material;
            boolean hasPermission = player.hasPermission(permission) || player.hasPermission("armortrims.admin");

            Material icon = hasPermission ? MATERIAL_ICONS[i] : Material.BARRIER;
            ItemStack item = new ItemStack(icon);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.displayName(Component.text(
                        (hasPermission ? "§a" : "§c") +
                        plugin.getConfigManager().getMessage("materials." + material)
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
        inventory.setItem(25, backItem);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();

        if (slot == 25) {
            guiManager.openMainGui(player);
            return;
        }

        if (slot < MATERIALS.length) {
            String material = MATERIALS[slot];
            String permission = "armortrims.material." + material;

            if (!player.hasPermission(permission) && !player.hasPermission("armortrims.admin")) {
                player.sendMessage(plugin.getConfigManager().getPrefix() +
                        plugin.getConfigManager().getMessage("permissions.material-locked"));
                return;
            }

            // Save the selection
            player.sendMessage(plugin.getConfigManager().getPrefix() +
                    plugin.getConfigManager().getMessage("commands.trim-selected"));
            player.closeInventory();
        }
    }
}