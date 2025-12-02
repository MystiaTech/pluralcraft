package com.pluralcraft.compat;

import com.pluralcraft.data.BodyCustomization;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Compatibility helper for Minecraft Comes Alive (MCA) Reborn
 * Detects if the mod is loaded AND allows us to modify their villager/player customization data!
 */
public class MCACompat {
    private static Boolean MCA_LOADED = null;
    private static Class<?> PLAYER_DATA_CLASS = null;
    private static Class<?> VILLAGER_ENTITY_MCA_CLASS = null;

    /**
     * Check if MCA Reborn is loaded
     *
     * NOTE: MCA Reborn is NOT compatible with PluralCraft's body customization!
     * MCA renders breasts independently and cannot be controlled or disabled by our mod.
     *
     * We always return false to indicate no integration is available.
     * Users must choose between MCA Reborn OR body customization, not both.
     */
    public static boolean isMCALoaded() {
        if (MCA_LOADED == null) {
            // Check if MCA is installed
            boolean mcaInstalled = ModList.get().isLoaded("mca");

            if (mcaInstalled) {
                System.out.println("[PluralCraft] ⚠ MCA Reborn detected!");
                System.out.println("[PluralCraft] MCA Reborn is not compatible with body customization.");
                System.out.println("[PluralCraft] Please uninstall MCA or use Wildfire's Gender Mod instead.");
            }

            // Always return false - no integration possible
            MCA_LOADED = false;
        }
        return MCA_LOADED;
    }

    /**
     * Apply our body customization to MCA!
     * This MODIFIES MCA's data so our alter's body shows up!
     *
     * NOTE: MCA Reborn primarily customizes VILLAGERS, not the player character
     * This method is experimental and may need adjustment based on MCA's actual API
     */
    public static boolean applyBodyToMCA(Player player, BodyCustomization body) {
        if (!isMCALoaded()) {
            return false;
        }

        try {
            // MCA Reborn primarily handles villager customization, not player
            // The player's appearance in MCA is usually just the skin
            // Body customization (breast size, height, etc.) is mainly for villagers

            // However, if MCA has a PlayerData class with customization options, we can try:
            if (PLAYER_DATA_CLASS != null) {
                // Try to get PlayerData instance for this player
                // Common patterns: PlayerData.get(player), PlayerData.getPlayerData(player)

                Method getPlayerDataMethod = null;
                try {
                    getPlayerDataMethod = PLAYER_DATA_CLASS.getMethod("get", Player.class);
                } catch (NoSuchMethodException e) {
                    try {
                        getPlayerDataMethod = PLAYER_DATA_CLASS.getMethod("getPlayerData", Player.class);
                    } catch (NoSuchMethodException e2) {
                        // Method not found
                        return false;
                    }
                }

                if (getPlayerDataMethod != null) {
                    Object playerData = getPlayerDataMethod.invoke(null, player);

                    if (playerData != null) {
                        // Try to set breast size or similar fields
                        // MCA might use different field names: breastSize, bustSize, bodyShape, etc.
                        boolean anySuccess = false;

                        // Try common field names
                        String[] fieldNames = {"breastSize", "bustSize", "breasts", "bodyBreasts"};
                        for (String fieldName : fieldNames) {
                            try {
                                Field field = PLAYER_DATA_CLASS.getDeclaredField(fieldName);
                                field.setAccessible(true);
                                field.setFloat(playerData, body.isCustomBodyEnabled() ? body.getBreastSize() : 0.0f);
                                anySuccess = true;
                                break; // Found and set the field!
                            } catch (NoSuchFieldException e) {
                                // Try next field name
                            }
                        }

                        if (anySuccess) {
                            // Try to sync if there's a sync method
                            try {
                                Method syncMethod = PLAYER_DATA_CLASS.getMethod("sync");
                                syncMethod.invoke(playerData);
                            } catch (NoSuchMethodException e) {
                                // Sync method doesn't exist, that's okay
                            }

                            return true;
                        }
                    }
                }
            }

            return false;

        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("[PluralCraft] Failed to apply body to MCA: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get a message about MCA integration status
     */
    public static String getMCAMessage() {
        if (isMCALoaded()) {
            if (PLAYER_DATA_CLASS != null) {
                return "MCA Reborn detected! PluralCraft will try to sync body settings.";
            } else {
                return "MCA Reborn detected! (Note: MCA primarily customizes villagers, not players)";
            }
        }
        return "";
    }
}
