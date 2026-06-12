package com.custom.armortrims.managers;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.gui.ArmorSelectionGui;
import com.custom.armortrims.gui.MaterialSelectionGui;
import com.custom.armortrims.gui.PatternSelectionGui;
import com.custom.armortrims.models.PlayerArmorTrim;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {
    private final CustomArmorTrims plugin;
    private final ConfigurationManager configManager;
    private final PlayerDataManager playerDataManager;
    private final Map<UUID, String> playerGuiContext;

    public GuiManager(CustomArmorTrims plugin, ConfigurationManager configManager, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerDataManager = playerDataManager;
        this.playerGuiContext = new HashMap<>();
    }

    public void openMainGui(Player player) {
        new ArmorSelectionGui(plugin, this, player).open();
    }

    public void openPatternGui(Player player, String armorPiece) {
        new PatternSelectionGui(plugin, this, player, armorPiece).open();
    }

    public void openMaterialGui(Player player, String armorPiece) {
        new MaterialSelectionGui(plugin, this, player, armorPiece).open();
    }

    public void setPlayerGuiContext(UUID playerUuid, String context) {
        playerGuiContext.put(playerUuid, context);
    }

    public String getPlayerGuiContext(UUID playerUuid) {
        return playerGuiContext.getOrDefault(playerUuid, "");
    }

    public void clearPlayerGuiContext(UUID playerUuid) {
        playerGuiContext.remove(playerUuid);
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}