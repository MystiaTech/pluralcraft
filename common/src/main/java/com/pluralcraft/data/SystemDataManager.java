package com.pluralcraft.data;

import com.pluralcraft.PluralCraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all system profiles on the server
 * Handles saving/loading from disk
 */
@Mod.EventBusSubscriber(modid = PluralCraft.MOD_ID)
public class SystemDataManager {
    private static final Map<UUID, SystemProfile> PROFILES = new HashMap<>();
    private static File saveFile;

    /**
     * Get or create a system profile for a player
     */
    public static SystemProfile getOrCreateProfile(UUID playerUUID, String defaultSystemName) {
        return PROFILES.computeIfAbsent(playerUUID, uuid -> {
            PluralCraft.LOGGER.info("Creating new system profile for player: {}", playerUUID);
            return new SystemProfile(defaultSystemName, uuid);
        });
    }

    /**
     * Get a profile if it exists (returns null if not found)
     */
    public static SystemProfile getProfile(UUID playerUUID) {
        return PROFILES.get(playerUUID);
    }

    /**
     * Check if a player has a profile
     */
    public static boolean hasProfile(UUID playerUUID) {
        return PROFILES.containsKey(playerUUID);
    }

    /**
     * Remove a profile (mainly for testing)
     */
    public static void removeProfile(UUID playerUUID) {
        PROFILES.remove(playerUUID);
    }

    /**
     * Load all profiles from disk when server starts
     */
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        saveFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), "pluralcraft_data.dat");

        if (saveFile.exists()) {
            try {
                CompoundTag rootTag = NbtIo.readCompressed(saveFile);
                loadFromNBT(rootTag);
                PluralCraft.LOGGER.info("Loaded PluralCraft data for {} systems", PROFILES.size());
            } catch (IOException e) {
                PluralCraft.LOGGER.error("Failed to load PluralCraft data!", e);
            }
        } else {
            PluralCraft.LOGGER.info("No existing PluralCraft data found, starting fresh!");
        }
    }

    /**
     * Save all profiles to disk when server stops
     */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        if (saveFile != null) {
            try {
                CompoundTag rootTag = saveToNBT();
                NbtIo.writeCompressed(rootTag, saveFile);
                PluralCraft.LOGGER.info("Saved PluralCraft data for {} systems", PROFILES.size());
            } catch (IOException e) {
                PluralCraft.LOGGER.error("Failed to save PluralCraft data!", e);
            }
        }
    }

    /**
     * Save all profiles to NBT
     */
    private static CompoundTag saveToNBT() {
        CompoundTag rootTag = new CompoundTag();
        CompoundTag profilesTag = new CompoundTag();

        for (Map.Entry<UUID, SystemProfile> entry : PROFILES.entrySet()) {
            profilesTag.put(entry.getKey().toString(), entry.getValue().toNBT());
        }

        rootTag.put("profiles", profilesTag);
        return rootTag;
    }

    /**
     * Load all profiles from NBT
     */
    private static void loadFromNBT(CompoundTag rootTag) {
        PROFILES.clear();
        CompoundTag profilesTag = rootTag.getCompound("profiles");

        for (String uuidString : profilesTag.getAllKeys()) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                SystemProfile profile = SystemProfile.fromNBT(profilesTag.getCompound(uuidString));
                PROFILES.put(uuid, profile);
            } catch (IllegalArgumentException e) {
                PluralCraft.LOGGER.error("Invalid UUID in save data: {}", uuidString);
            }
        }
    }

    /**
     * Manually save data (can be called anytime)
     */
    public static void saveData() {
        if (saveFile != null) {
            try {
                CompoundTag rootTag = saveToNBT();
                NbtIo.writeCompressed(rootTag, saveFile);
                PluralCraft.LOGGER.info("Manually saved PluralCraft data");
            } catch (IOException e) {
                PluralCraft.LOGGER.error("Failed to manually save PluralCraft data!", e);
            }
        }
    }
}
