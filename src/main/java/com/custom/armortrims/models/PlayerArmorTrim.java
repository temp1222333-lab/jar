package com.custom.armortrims.models;

import java.util.UUID;

public class PlayerArmorTrim {
    private final UUID playerUuid;
    private String pattern;
    private String material;
    private long lastUpdated;

    public PlayerArmorTrim(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.lastUpdated = System.currentTimeMillis();
    }

    public PlayerArmorTrim(UUID playerUuid, String pattern, String material) {
        this.playerUuid = playerUuid;
        this.pattern = pattern;
        this.material = material;
        this.lastUpdated = System.currentTimeMillis();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public boolean hasValidTrim() {
        return pattern != null && material != null && !pattern.isEmpty() && !material.isEmpty();
    }

    @Override
    public String toString() {
        return "PlayerArmorTrim{" +
                "playerUuid=" + playerUuid +
                ", pattern='" + pattern + '\'' +
                ", material='" + material + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}