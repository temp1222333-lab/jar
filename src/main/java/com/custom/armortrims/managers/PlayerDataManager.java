package com.custom.armortrims.managers;

import com.custom.armortrims.CustomArmorTrims;
import com.custom.armortrims.database.DatabaseManager;
import com.custom.armortrims.models.PlayerArmorTrim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerDataManager {
    private final CustomArmorTrims plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerArmorTrim> playerDataCache;
    private final Map<UUID, Long> cacheTimestamps;

    public PlayerDataManager(CustomArmorTrims plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerDataCache = new HashMap<>();
        this.cacheTimestamps = new HashMap<>();
    }

    public CompletableFuture<PlayerArmorTrim> getPlayerArmorTrim(UUID playerUuid) {
        if (plugin.getConfigManager().isCachePlayerDataEnabled()) {
            long now = System.currentTimeMillis();
            long cacheTtl = plugin.getConfigManager().getCacheTtlMinutes() * 60000L;

            if (playerDataCache.containsKey(playerUuid)) {
                long cacheAge = now - cacheTimestamps.get(playerUuid);
                if (cacheAge < cacheTtl) {
                    return CompletableFuture.completedFuture(playerDataCache.get(playerUuid));
                } else {
                    playerDataCache.remove(playerUuid);
                    cacheTimestamps.remove(playerUuid);
                }
            }
        }

        return databaseManager.getPlayerArmorTrim(playerUuid)
                .thenApply(trim -> {
                    if (trim != null && plugin.getConfigManager().isCachePlayerDataEnabled()) {
                        playerDataCache.put(playerUuid, trim);
                        cacheTimestamps.put(playerUuid, System.currentTimeMillis());
                    }
                    return trim;
                });
    }

    public CompletableFuture<Void> savePlayerArmorTrim(PlayerArmorTrim trim) {
        return databaseManager.savePlayerArmorTrim(trim)
                .thenApply(v -> {
                    playerDataCache.put(trim.getPlayerUuid(), trim);
                    cacheTimestamps.put(trim.getPlayerUuid(), System.currentTimeMillis());
                    return v;
                });
    }

    public CompletableFuture<Void> setPattern(UUID playerUuid, String pattern) {
        return getPlayerArmorTrim(playerUuid)
                .thenCompose(trim -> {
                    if (trim == null) {
                        trim = new PlayerArmorTrim(playerUuid);
                    }
                    trim.setPattern(pattern);
                    return savePlayerArmorTrim(trim);
                });
    }

    public CompletableFuture<Void> setMaterial(UUID playerUuid, String material) {
        return getPlayerArmorTrim(playerUuid)
                .thenCompose(trim -> {
                    if (trim == null) {
                        trim = new PlayerArmorTrim(playerUuid);
                    }
                    trim.setMaterial(material);
                    return savePlayerArmorTrim(trim);
                });
    }

    public CompletableFuture<Void> resetPlayerArmorTrim(UUID playerUuid) {
        return databaseManager.deletePlayerArmorTrim(playerUuid)
                .thenApply(v -> {
                    playerDataCache.remove(playerUuid);
                    cacheTimestamps.remove(playerUuid);
                    return v;
                });
    }

    public void clearCache(UUID playerUuid) {
        playerDataCache.remove(playerUuid);
        cacheTimestamps.remove(playerUuid);
    }

    public void clearAllCache() {
        playerDataCache.clear();
        cacheTimestamps.clear();
    }
}