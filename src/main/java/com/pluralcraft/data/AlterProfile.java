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

    public AlterProfile(String name, int age, String pronouns, String bio) {
        this.name = name;
        this.age = age;
        this.pronouns = pronouns;
        this.bio = bio;
        this.skinURL = "";
        this.usePronounDB = false;
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPronouns() { return pronouns; }
    public String getBio() { return bio; }
    public String getSkinURL() { return skinURL; }
    public boolean isUsePronounDB() { return usePronounDB; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setPronouns(String pronouns) { this.pronouns = pronouns; }
    public void setBio(String bio) { this.bio = bio; }
    public void setSkinURL(String skinURL) { this.skinURL = skinURL; }
    public void setUsePronounDB(boolean usePronounDB) { this.usePronounDB = usePronounDB; }

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
