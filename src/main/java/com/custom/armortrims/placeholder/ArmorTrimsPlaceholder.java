package com.custom.armortrims.placeholder;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.models.PlayerArmorTrim;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class ArmorTrimsPlaceholder extends PlaceholderExpansion {
    private final CustomArmorTrims plugin;

    public ArmorTrimsPlaceholder(CustomArmorTrims plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "armortrim";
    }

    @Override
    public String getAuthor() {
        return "CustomPluginDev";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }

        try {
            PlayerArmorTrim trim = plugin.getPlayerDataManager()
                    .getPlayerArmorTrim(player.getUniqueId())
                    .join();

            if (trim == null || !trim.hasValidTrim()) {
                return plugin.getConfigManager().getMessage("info.no-trim-selected");
            }

            return switch (params.toLowerCase()) {
                case "pattern" -> plugin.getConfigManager().getMessage("patterns." + trim.getPattern());
                case "material" -> plugin.getConfigManager().getMessage("materials." + trim.getMaterial());
                case "full" -> plugin.getConfigManager().getMessage("patterns." + trim.getPattern()) + " " +
                        plugin.getConfigManager().getMessage("materials." + trim.getMaterial());
                default -> "";
            };
        } catch (Exception e) {
            plugin.logError("Error in PlaceholderAPI expansion", e);
            return "";
        }
    }
}