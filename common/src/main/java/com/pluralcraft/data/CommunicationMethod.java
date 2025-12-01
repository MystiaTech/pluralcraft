package com.pluralcraft.data;

/**
 * Different communication methods an alter might use
 */
public enum CommunicationMethod {
    VERBAL("Verbal", "Speaks normally"),
    NONVERBAL_EMOTES("Nonverbal (Emotes)", "Communicates through emotes and gestures"),
    TEXT_ONLY("Text Only", "Only communicates through text/signs"),
    SELECTIVE_MUTE("Selectively Mute", "Speaks only in certain situations"),
    MIXED("Mixed", "Uses multiple communication methods");

    private final String displayName;
    private final String description;

    CommunicationMethod(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get from string name (for NBT loading)
     */
    public static CommunicationMethod fromString(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return VERBAL; // Default
        }
    }
}
