package com.pluralcraft.data;

import net.minecraft.nbt.CompoundTag;

/**
 * Represents a single alter's profile within a system
 */
public class AlterProfile {
    private String name;
    private int age;
    private String pronouns;
    private String bio;
    private String skinURL; // Optional custom skin for this alter
    private boolean usePronounDB;
    private CommunicationMethod communicationMethod;
    private BodyCustomization bodyCustomization; // Body appearance settings

    // Mod settings storage - stores configuration for other mods per alter
    // Key: mod ID (e.g., "wildfire_gender", "cosmeticarmor")
    // Value: NBT data for that mod's settings
    private CompoundTag modSettings;

    public AlterProfile(String name, int age, String pronouns, String bio) {
        this.name = name;
        this.age = age;
        this.pronouns = pronouns;
        this.bio = bio;
        this.skinURL = "";
        this.usePronounDB = false;
        this.communicationMethod = CommunicationMethod.VERBAL; // Default
        this.bodyCustomization = new BodyCustomization(); // Default body
        this.modSettings = new CompoundTag();
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPronouns() { return pronouns; }
    public String getBio() { return bio; }
    public String getSkinURL() { return skinURL; }
    public boolean isUsePronounDB() { return usePronounDB; }
    public CommunicationMethod getCommunicationMethod() { return communicationMethod; }
    public BodyCustomization getBodyCustomization() { return bodyCustomization; }
    public CompoundTag getModSettings() { return modSettings; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setPronouns(String pronouns) { this.pronouns = pronouns; }
    public void setBio(String bio) { this.bio = bio; }
    public void setSkinURL(String skinURL) { this.skinURL = skinURL; }
    public void setUsePronounDB(boolean usePronounDB) { this.usePronounDB = usePronounDB; }
    public void setCommunicationMethod(CommunicationMethod method) { this.communicationMethod = method; }
    public void setBodyCustomization(BodyCustomization body) { this.bodyCustomization = body; }
    public void setModSettings(CompoundTag modSettings) { this.modSettings = modSettings; }

    /**
     * Save settings for a specific mod
     * @param modId The mod's ID (e.g., "wildfire_gender")
     * @param settings The settings as NBT
     */
    public void setModSetting(String modId, CompoundTag settings) {
        modSettings.put(modId, settings);
    }

    /**
     * Get settings for a specific mod
     * @param modId The mod's ID
     * @return The settings, or an empty tag if not found
     */
    public CompoundTag getModSetting(String modId) {
        if (modSettings.contains(modId)) {
            return modSettings.getCompound(modId);
        }
        return new CompoundTag();
    }

    /**
     * Check if this alter has settings saved for a specific mod
     * @param modId The mod's ID
     * @return True if settings exist
     */
    public boolean hasModSetting(String modId) {
        return modSettings.contains(modId);
    }

    /**
     * Save this alter profile to NBT (Minecraft's data format)
     */
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putInt("age", age);
        tag.putString("pronouns", pronouns);
        tag.putString("bio", bio);
        tag.putString("skinURL", skinURL);
        tag.putBoolean("usePronounDB", usePronounDB);
        tag.putString("communicationMethod", communicationMethod.name());
        tag.put("bodyCustomization", bodyCustomization.saveToNBT());
        tag.put("modSettings", modSettings);
        return tag;
    }

    /**
     * Load an alter profile from NBT
     */
    public static AlterProfile fromNBT(CompoundTag tag) {
        AlterProfile profile = new AlterProfile(
            tag.getString("name"),
            tag.getInt("age"),
            tag.getString("pronouns"),
            tag.getString("bio")
        );
        profile.setSkinURL(tag.getString("skinURL"));
        profile.setUsePronounDB(tag.getBoolean("usePronounDB"));
        if (tag.contains("communicationMethod")) {
            profile.setCommunicationMethod(CommunicationMethod.fromString(tag.getString("communicationMethod")));
        }
        if (tag.contains("bodyCustomization")) {
            profile.setBodyCustomization(BodyCustomization.fromNBT(tag.getCompound("bodyCustomization")));
        }
        if (tag.contains("modSettings")) {
            profile.setModSettings(tag.getCompound("modSettings"));
        }
        return profile;
    }

    /**
     * Get a formatted display name for chat
     * Format: [SystemName] AlterName [pronouns]
     */
    public String getFormattedName(String systemName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(systemName).append("] ");
        sb.append(name);
        if (!pronouns.isEmpty() && !usePronounDB) {
            sb.append(" [").append(pronouns).append("]");
        }
        return sb.toString();
    }
}
