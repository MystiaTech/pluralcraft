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

            // Wildfire uses an attachment/capability system
            // We need to get the GenderPlayer from the player's capability
            try {
                // Try to get GenderPlayer via getGender() or similar
                // Wildfire stores data as a capability on the player entity

                // First, try getting via static method with Player parameter
                Method getGenderMethod = null;
                try {
                    getGenderMethod = GENDER_PLAYER_CLASS.getMethod("getGenderPlayer", Player.class);
                } catch (NoSuchMethodException e1) {
                    try {
                        getGenderMethod = GENDER_PLAYER_CLASS.getMethod("get", Player.class);
                    } catch (NoSuchMethodException e2) {
                        // Wildfire might use a registry or provider pattern
                        System.err.println("[PluralCraft] Can't find method to get GenderPlayer from Player");
                        System.err.println("[PluralCraft] Trying direct updateBustSize call...");

                        // Last resort: Try calling updateBustSize/updateGender directly with player
                        float newValue = body.isCustomBodyEnabled() ? body.getBreastSize() : 0.0f;

                        // Look for static methods that take Player
                        for (Method m : GENDER_PLAYER_CLASS.getMethods()) {
                            if (m.getName().equals("updateBustSize") && m.getParameterCount() == 2) {
                                System.out.println("[PluralCraft] Found updateBustSize with 2 params, trying...");
                                m.invoke(null, player, newValue);
                                System.out.println("[PluralCraft] Called updateBustSize via static method!");
                                return true;
                            }
                        }

                        System.err.println("[PluralCraft] Could not find way to update Wildfire data");
                        return false;
                    }
                }

                if (getGenderMethod != null) {
                    Object genderPlayer = getGenderMethod.invoke(null, player);

                    if (genderPlayer != null) {
                        System.out.println("[PluralCraft] Found GenderPlayer instance!");

                        // Use updateBustSize(float) to set the breast size!
                        float newValue = body.isCustomBodyEnabled() ? body.getBreastSize() : 0.0f;
                        Method updateBustMethod = GENDER_PLAYER_CLASS.getMethod("updateBustSize", float.class);
                        updateBustMethod.invoke(genderPlayer, newValue);
                        System.out.println("[PluralCraft] Called updateBustSize(" + newValue + ")");

                        // Save the changes
                        try {
                            Method saveMethod = GENDER_PLAYER_CLASS.getMethod("saveGenderInfo");
                            saveMethod.invoke(genderPlayer);
                            System.out.println("[PluralCraft] Called saveGenderInfo()!");
                        } catch (NoSuchMethodException e) {
                            System.out.println("[PluralCraft] No saveGenderInfo, trying updateGender...");
                            try {
                                Method updateMethod = GENDER_PLAYER_CLASS.getMethod("updateGender");
                                updateMethod.invoke(genderPlayer);
                            } catch (NoSuchMethodException e2) {
                                // Changes might auto-sync
                            }
                        }

                        return true;
                    }
                }
            } catch (NoSuchMethodException e) {
                System.err.println("[PluralCraft] Method not found: " + e.getMessage());
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
