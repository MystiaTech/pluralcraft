package com.pluralcraft.compat;

import net.minecraftforge.fml.ModList;

/**
 * Compatibility helper for Wildfire's Gender Mod
 * Detects if the mod is loaded to avoid conflicts
 */
public class WildfireCompat {
    private static Boolean WILDFIRE_LOADED = null;

    /**
     * Check if Wildfire's Gender Mod is loaded
     */
    public static boolean isWildfireLoaded() {
        if (WILDFIRE_LOADED == null) {
            WILDFIRE_LOADED = ModList.get().isLoaded("wildfire_gender") ||
                             ModList.get().isLoaded("wildfire_female_gender_mod");
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
}
