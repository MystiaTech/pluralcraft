package com.pluralcraft.integration;

import net.minecraft.nbt.CompoundTag;

/**
 * Interface for mod integrations
 * Implement this for each mod you want to support
 */
public interface IModIntegration {
    /**
     * Capture the current settings from this mod
     * @return NBT tag containing all relevant settings
     */
    CompoundTag captureCurrentSettings();

    /**
     * Apply settings to this mod
     * @param settings NBT tag containing the settings to apply
     */
    void applySettings(CompoundTag settings);

    /**
     * Get the mod ID this integration is for
     * @return The mod's ID (e.g., "wildfire_gender")
     */
    String getModId();

    /**
     * Get a human-readable name for this integration
     * @return Display name (e.g., "Wildfire's Gender Mod")
     */
    String getDisplayName();
}
