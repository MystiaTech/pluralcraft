package com.pluralcraft.integration;

import com.pluralcraft.PluralCraft;
import com.pluralcraft.data.AlterProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles integration with other mods for alter-specific settings
 *
 * Supported mods:
 * - Wildfire's Female Gender Mod (wildfire_gender)
 * - Cosmetic Armor (cosmeticarmor)
 * - More coming soon!
 */
public class ModIntegration {
    private static final Map<String, IModIntegration> INTEGRATIONS = new HashMap<>();

    /**
     * Check which supported mods are installed and register integrations
     */
    public static void initializeIntegrations() {
        PluralCraft.LOGGER.info("Checking for mod integrations...");

        // Check for Wildfire's Gender Mod using our compat class
        if (com.pluralcraft.compat.WildfireCompat.isWildfireLoaded()) {
            PluralCraft.LOGGER.info("✓ Wildfire's Gender Mod integration available!");
        }

        // Check for MCA Reborn using our compat class
        if (com.pluralcraft.compat.MCACompat.isMCALoaded()) {
            PluralCraft.LOGGER.info("✓ MCA Reborn integration available!");
        }

        // Check for Cosmetic Armor
        if (ModList.get().isLoaded("cosmeticarmor")) {
            PluralCraft.LOGGER.info("Cosmetic Armor detected! (No integration yet)");
        }

        // Note: INTEGRATIONS map is for the old system, we now use WildfireCompat/MCACompat directly
        PluralCraft.LOGGER.info("Mod integration check complete!");
    }

    /**
     * Save current settings from all integrated mods to an alter profile
     */
    public static void saveCurrentSettingsToAlter(AlterProfile alter) {
        for (Map.Entry<String, IModIntegration> entry : INTEGRATIONS.entrySet()) {
            String modId = entry.getKey();
            IModIntegration integration = entry.getValue();

            try {
                CompoundTag settings = integration.captureCurrentSettings();
                alter.setModSetting(modId, settings);
                PluralCraft.LOGGER.debug("Saved {} settings for alter {}", modId, alter.getName());
            } catch (Exception e) {
                PluralCraft.LOGGER.error("Failed to save {} settings for alter {}", modId, alter.getName(), e);
            }
        }
    }

    /**
     * Apply an alter's saved settings to all integrated mods
     */
    public static void applyAlterSettings(AlterProfile alter) {
        for (Map.Entry<String, IModIntegration> entry : INTEGRATIONS.entrySet()) {
            String modId = entry.getKey();
            IModIntegration integration = entry.getValue();

            if (alter.hasModSetting(modId)) {
                try {
                    CompoundTag settings = alter.getModSetting(modId);
                    integration.applySettings(settings);
                    PluralCraft.LOGGER.debug("Applied {} settings for alter {}", modId, alter.getName());
                } catch (Exception e) {
                    PluralCraft.LOGGER.error("Failed to apply {} settings for alter {}", modId, alter.getName(), e);
                }
            }
        }
    }

    /**
     * Check if a specific mod integration is available
     */
    public static boolean hasIntegration(String modId) {
        return INTEGRATIONS.containsKey(modId);
    }

    /**
     * Get list of all available integrations
     */
    public static Map<String, IModIntegration> getIntegrations() {
        return new HashMap<>(INTEGRATIONS);
    }
}
