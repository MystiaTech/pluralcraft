package com.pluralcraft.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a complete system profile with all alters
 */
public class SystemProfile {
    private String systemName;
    private List<AlterProfile> alters;
    private int currentAlterIndex;
    private UUID ownerUUID; // The Minecraft player this system belongs to

    public SystemProfile(String systemName, UUID ownerUUID) {
        this.systemName = systemName;
        this.ownerUUID = ownerUUID;
        this.alters = new ArrayList<>();
        this.currentAlterIndex = 0;
    }

    // Getters
    public String getSystemName() { return systemName; }
    public List<AlterProfile> getAlters() { return alters; }
    public int getCurrentAlterIndex() { return currentAlterIndex; }
    public UUID getOwnerUUID() { return ownerUUID; }

    /**
     * Get the currently active alter
     */
    public AlterProfile getCurrentAlter() {
        if (alters.isEmpty()) {
            return null;
        }
        if (currentAlterIndex >= alters.size()) {
            currentAlterIndex = 0;
        }
        return alters.get(currentAlterIndex);
    }

    // Setters
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public void setCurrentAlterIndex(int index) {
        if (index >= 0 && index < alters.size()) {
            this.currentAlterIndex = index;
        }
    }

    // Alter management
    public void addAlter(AlterProfile alter) {
        alters.add(alter);
    }

    public void removeAlter(int index) {
        if (index >= 0 && index < alters.size()) {
            alters.remove(index);
            if (currentAlterIndex >= alters.size() && !alters.isEmpty()) {
                currentAlterIndex = alters.size() - 1;
            }
        }
    }

    /**
     * Switch to the next alter in the list
     */
    public void switchToNextAlter() {
        if (!alters.isEmpty()) {
            currentAlterIndex = (currentAlterIndex + 1) % alters.size();
        }
    }

    /**
     * Switch to a specific alter by name
     */
    public boolean switchToAlter(String name) {
        for (int i = 0; i < alters.size(); i++) {
            if (alters.get(i).getName().equalsIgnoreCase(name)) {
                currentAlterIndex = i;
                return true;
            }
        }
        return false;
    }

    /**
     * Save this system profile to NBT
     */
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("systemName", systemName);
        tag.putUUID("ownerUUID", ownerUUID);
        tag.putInt("currentAlterIndex", currentAlterIndex);

        ListTag altersList = new ListTag();
        for (AlterProfile alter : alters) {
            altersList.add(alter.toNBT());
        }
        tag.put("alters", altersList);

        return tag;
    }

    /**
     * Load a system profile from NBT
     */
    public static SystemProfile fromNBT(CompoundTag tag) {
        UUID uuid = tag.getUUID("ownerUUID");
        SystemProfile profile = new SystemProfile(tag.getString("systemName"), uuid);
        profile.currentAlterIndex = tag.getInt("currentAlterIndex");

        ListTag altersList = tag.getList("alters", Tag.TAG_COMPOUND);
        for (int i = 0; i < altersList.size(); i++) {
            CompoundTag alterTag = altersList.getCompound(i);
            profile.addAlter(AlterProfile.fromNBT(alterTag));
        }

        return profile;
    }
}
