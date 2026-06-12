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

public class ArmorSelectionGui {
    private final CustomArmorTrims plugin;
    private final GuiManager guiManager;
    private final Player player;
    private Inventory inventory;

    public ArmorSelectionGui(CustomArmorTrims plugin, GuiManager guiManager, Player player) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.player = player;
    }

    public void open() {
        inventory = plugin.getServer().createInventory(null, 27, plugin.getConfigManager().getMessage("gui.main-title"));
        setupItems();
        player.openInventory(inventory);

        // Play open sound
        if (plugin.getConfigManager().isSoundsEnabled()) {
            try {
                player.playSound(player.getLocation(),
                        org.bukkit.Sound.valueOf(plugin.getConfigManager().getGuiOpenSound()),
                        1.0f, 1.0f);
            } catch (Exception e) {
                // Sound not available
            }
        }
    }

    private void setupItems() {
        // Helmet
        ItemStack helmet = createArmorItem(Material.IRON_HELMET, "gui.armor-piece-title", 0);
        ItemMeta helmetMeta = helmet.getItemMeta();
        if (helmetMeta != null) {
            helmetMeta.displayName(Component.text(plugin.getConfigManager().getMessage("armor-pieces.helmet")));
            helmet.setItemMeta(helmetMeta);
        }
        inventory.setItem(11, helmet);

        // Chestplate
        ItemStack chestplate = createArmorItem(Material.IRON_CHESTPLATE, "gui.armor-piece-title", 1);
        ItemMeta chestplateMeta = chestplate.getItemMeta();
        if (chestplateMeta != null) {
            chestplateMeta.displayName(Component.text(plugin.getConfigManager().getMessage("armor-pieces.chestplate")));
            chestplate.setItemMeta(chestplateMeta);
        }
        inventory.setItem(13, chestplate);

        // Leggings
        ItemStack leggings = createArmorItem(Material.IRON_LEGGINGS, "gui.armor-piece-title", 2);
        ItemMeta leggingsMeta = leggings.getItemMeta();
        if (leggingsMeta != null) {
            leggingsMeta.displayName(Component.text(plugin.getConfigManager().getMessage("armor-pieces.leggings")));
            leggings.setItemMeta(leggingsMeta);
        }
        inventory.setItem(15, leggings);

        // Boots
        ItemStack boots = createArmorItem(Material.IRON_BOOTS, "gui.armor-piece-title", 3);
        ItemMeta bootsMeta = boots.getItemMeta();
        if (bootsMeta != null) {
            bootsMeta.displayName(Component.text(plugin.getConfigManager().getMessage("armor-pieces.boots")));
            boots.setItemMeta(bootsMeta);
        }
        inventory.setItem(17, boots);
    }

    private ItemStack createArmorItem(Material material, String lore, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§e" + material.name()));
            item.setItemMeta(meta);
        }
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();

        switch (slot) {
            case 11 -> guiManager.openPatternGui(player, "helmet");
            case 13 -> guiManager.openPatternGui(player, "chestplate");
            case 15 -> guiManager.openPatternGui(player, "leggings");
            case 17 -> guiManager.openPatternGui(player, "boots");
        }
    }
}