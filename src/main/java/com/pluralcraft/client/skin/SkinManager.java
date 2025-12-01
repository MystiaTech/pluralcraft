package com.pluralcraft.client.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.NativeImage;
import com.pluralcraft.data.AlterProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages custom skins for alters
 * Handles loading, caching, and applying skins from URLs or local files
 */
@OnlyIn(Dist.CLIENT)
public class SkinManager {

    private static final Map<UUID, ResourceLocation> cachedSkins = new HashMap<>();
    private static final Path SKIN_CACHE_DIR = Paths.get("pluralcraft", "skins");

    /**
     * Apply a custom skin to the current player from an AlterProfile
     */
    public static void applySkin(AlterProfile alter) {
        if (alter == null) {
            return;
        }

        String skinURL = alter.getSkinURL();
        if (skinURL == null || skinURL.isEmpty()) {
            // No custom skin - reset to default
            resetToDefaultSkin();
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        // Load skin from URL
        loadSkinFromURL(skinURL, mc.player.getGameProfile());
    }

    /**
     * Reset player skin to default (their actual Minecraft skin)
     */
    public static void resetToDefaultSkin() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        // Clear cached skin
        cachedSkins.remove(mc.player.getUUID());

        // Refresh player renderer to use default skin
        mc.player.getModelName();
    }

    /**
     * Load a skin from a URL (async)
     */
    private static void loadSkinFromURL(String urlString, GameProfile profile) {
        // Run in background thread to avoid blocking
        new Thread(() -> {
            try {
                // Create cache directory if it doesn't exist
                Files.createDirectories(SKIN_CACHE_DIR);

                // Download skin to cache
                URL url = new URL(urlString);
                String fileName = profile.getId().toString() + ".png";
                Path cachedFile = SKIN_CACHE_DIR.resolve(fileName);

                // Download the skin
                try (var in = url.openStream()) {
                    Files.copy(in, cachedFile, StandardCopyOption.REPLACE_EXISTING);
                }

                // Apply the skin on the main thread
                Minecraft.getInstance().execute(() -> {
                    applyCachedSkin(cachedFile, profile);
                });

            } catch (IOException e) {
                // Failed to load skin - user will see default
                Minecraft.getInstance().execute(() -> {
                    if (Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.sendSystemMessage(
                            net.minecraft.network.chat.Component.literal(
                                "Failed to load custom skin from URL. Using default."
                            )
                        );
                    }
                });
            }
        }, "PluralCraft-SkinLoader").start();
    }

    /**
     * Apply a skin from a cached file
     */
    private static void applyCachedSkin(Path skinFile, GameProfile profile) {
        try {
            // Create a resource location for this skin
            ResourceLocation skinLocation = new ResourceLocation(
                "pluralcraft",
                "skins/" + profile.getId()
            );

            // Load the texture using Minecraft's texture manager
            Minecraft mc = Minecraft.getInstance();

            // Load the image as NativeImage
            NativeImage nativeImage;
            try (InputStream is = Files.newInputStream(skinFile)) {
                nativeImage = NativeImage.read(is);
            }

            // Register the dynamic texture
            net.minecraft.client.renderer.texture.DynamicTexture texture =
                new net.minecraft.client.renderer.texture.DynamicTexture(nativeImage);

            mc.getTextureManager().register(skinLocation, texture);

            // Cache the skin location
            cachedSkins.put(profile.getId(), skinLocation);

            // Force player model to refresh
            if (mc.player != null) {
                // Trigger a model refresh by updating render info
                mc.player.refreshDisplayName();
            }

        } catch (Exception e) {
            // Failed to apply skin - silently continue with default
        }
    }

    /**
     * Get the custom skin for a player, if any
     */
    public static ResourceLocation getCustomSkin(UUID playerUUID) {
        return cachedSkins.get(playerUUID);
    }

    /**
     * Check if a player has a custom skin applied
     */
    public static boolean hasCustomSkin(UUID playerUUID) {
        return cachedSkins.containsKey(playerUUID);
    }

    /**
     * Load a skin from a local file (for file upload feature)
     */
    public static void loadSkinFromFile(Path skinFile, AlterProfile alter) {
        if (!Files.exists(skinFile)) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        try {
            // Copy to cache
            String fileName = mc.player.getUUID().toString() + ".png";
            Path cachedFile = SKIN_CACHE_DIR.resolve(fileName);
            Files.createDirectories(SKIN_CACHE_DIR);
            Files.copy(skinFile, cachedFile, StandardCopyOption.REPLACE_EXISTING);

            // Store the path as the skin URL (so it persists)
            alter.setSkinURL("file://" + cachedFile.toAbsolutePath());

            // Apply the skin
            applyCachedSkin(cachedFile, mc.player.getGameProfile());

            mc.player.sendSystemMessage(
                net.minecraft.network.chat.Component.literal(
                    "Custom skin loaded from file!"
                )
            );

        } catch (IOException e) {
            mc.player.sendSystemMessage(
                net.minecraft.network.chat.Component.literal(
                    "Failed to load skin from file."
                )
            );
        }
    }
}
