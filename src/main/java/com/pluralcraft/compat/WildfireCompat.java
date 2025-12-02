package com.pluralcraft.compat;

import com.pluralcraft.data.BodyCustomization;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Compatibility helper for Wildfire's Gender Mod
 * Detects if the mod is loaded AND allows us to modify their data!
 */
public class WildfireCompat {
    private static Boolean WILDFIRE_LOADED = null;
    private static Class<?> GENDER_PLAYER_CLASS = null;
    private static Method GET_GENDER_PLAYER_METHOD = null;

    /**
     * Check if Wildfire's Gender Mod is loaded
     */
    public static boolean isWildfireLoaded() {
        if (WILDFIRE_LOADED == null) {
            WILDFIRE_LOADED = ModList.get().isLoaded("wildfire_gender") ||
                             ModList.get().isLoaded("wildfire_female_gender_mod");

            // Try to load their classes
            if (WILDFIRE_LOADED) {
                try {
                    GENDER_PLAYER_CLASS = Class.forName("com.wildfire.main.GenderPlayer");
                    // Try to find the method to get GenderPlayer from a Player
                    // This might be something like GenderPlayer.getPlayer(player)
                } catch (ClassNotFoundException e) {
                    WILDFIRE_LOADED = false;
                }
            }
        }
        return WILDFIRE_LOADED;
    }

    /**
     * Should we show our body customization?
     * Now returns TRUE even if Wildfire is loaded!
     * User requested: "allow customization with Wildfire present"
     */
    public static boolean shouldUseOurBodyCustomization() {
        return true; // Always allow our customization!
    }

    /**
     * Get a message about Wildfire integration
     */
    public static String getWildfireMessage() {
        if (isWildfireLoaded()) {
            return "Wildfire's Gender Mod detected! You can use either mod's customization.";
        }
        return "";
    }

    /**
     * Get button text for body customization
     */
    public static String getBodyCustomizationButtonText() {
        if (isWildfireLoaded()) {
            return "⚙ PluralCraft Body Settings";
        }
        return "⚙ Customize Body";
    }

    /**
     * Check if we should show Wildfire's button
     */
    public static boolean shouldShowWildfireButton() {
        return isWildfireLoaded();
    }

    /**
     * Get text for Wildfire's button
     */
    public static String getWildfireButtonText() {
        return "⚙ Wildfire's Gender Mod (Default)";
    }

    /**
     * Try to open Wildfire's GUI
     * Returns true if successful, false if we should show a message instead
     */
    public static boolean tryOpenWildfireGUI() {
        if (!isWildfireLoaded()) {
            return false;
        }

        try {
            // Try to use their keybinding via reflection
            // Wildfire uses KeyMappings which we can try to trigger
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();

            // Try to find their GUI opening method through reflection
            // This is a fallback - ideally we'd have their API
            Class<?> wildfireClass = Class.forName("com.wildfire.main.GenderPlayer");

            // If we got here, the mod exists but we can't open their GUI programmatically
            // Return false so the caller can show a message
            return false;

        } catch (ClassNotFoundException e) {
            // Wildfire not found or different version
            return false;
        } catch (Exception e) {
            // Any other error
            return false;
        }
    }

    /**
     * Get the message to show when we can't open Wildfire's GUI
     */
    public static String getWildfireGUIMessage() {
        return "Wildfire's Gender Mod detected! Check your keybinds to open their customization screen (usually 'G' key).";
    }

    /**
     * Apply our body customization to Wildfire's mod!
     * This MODIFIES Wildfire's data so our alter's body shows up!
     */
    public static boolean applyBodyToWildfire(Player player, BodyCustomization body) {
        if (!isWildfireLoaded() || GENDER_PLAYER_CLASS == null) {
            return false;
        }

        try {
            System.out.println("[PluralCraft] Attempting Wildfire integration...");

            // Try to get the GenderPlayer instance for this player
            // Wildfire might use an attachment or capability system
            // Common patterns: GenderPlayer.getPlayer(player) or player.getCapability()

            // Use Wildfire's actual API!
            // loadCachedPlayer(UUID) returns the GenderPlayer instance
            try {
                Method loadCachedMethod = GENDER_PLAYER_CLASS.getMethod("loadCachedPlayer", java.util.UUID.class);
                Object genderPlayer = loadCachedMethod.invoke(null, player.getUUID());

                if (genderPlayer != null) {
                    System.out.println("[PluralCraft] Found GenderPlayer instance via loadCachedPlayer!");

                    // Use updateBustSize(float) to set the breast size!
                    float newValue = body.isCustomBodyEnabled() ? body.getBreastSize() : 0.0f;
                    Method updateBustMethod = GENDER_PLAYER_CLASS.getMethod("updateBustSize", float.class);
                    updateBustMethod.invoke(genderPlayer, newValue);
                    System.out.println("[PluralCraft] Called updateBustSize(" + newValue + ")");

                    // Save the changes using saveGenderInfo()
                    try {
                        Method saveMethod = GENDER_PLAYER_CLASS.getMethod("saveGenderInfo");
                        saveMethod.invoke(genderPlayer);
                        System.out.println("[PluralCraft] Called saveGenderInfo() - changes saved!");
                    } catch (NoSuchMethodException e) {
                        System.out.println("[PluralCraft] saveGenderInfo() not found - changes may not persist");
                    }

                    return true;
                } else {
                    System.err.println("[PluralCraft] GenderPlayer is null! Player might not have gender data yet.");
                }
            } catch (NoSuchMethodException e) {
                System.err.println("[PluralCraft] loadCachedPlayer() method not found on GenderPlayer class");
                e.printStackTrace();
            }
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("[PluralCraft] Failed to apply body to Wildfire: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
