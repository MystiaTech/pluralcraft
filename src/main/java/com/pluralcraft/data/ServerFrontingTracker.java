package com.pluralcraft.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Server-side tracker for who's fronting for each player
 * This allows other players to query who someone is fronting as
 */
public class ServerFrontingTracker {
    private static final Map<UUID, FrontingInfo> frontingMap = new HashMap<>();

    /**
     * Update who a player is fronting as
     */
    public static void setFronting(UUID playerUUID, String alterName, String alterEmoji) {
        frontingMap.put(playerUUID, new FrontingInfo(alterName, alterEmoji));
    }

    /**
     * Get who a player is fronting as
     */
    public static FrontingInfo getFronting(UUID playerUUID) {
        return frontingMap.get(playerUUID);
    }

    /**
     * Remove a player from tracking (when they disconnect)
     */
    public static void removePlayer(UUID playerUUID) {
        frontingMap.remove(playerUUID);
    }

    /**
     * Clear all tracking data
     */
    public static void clear() {
        frontingMap.clear();
    }

    /**
     * Info about who's fronting
     */
    public static class FrontingInfo {
        public final String alterName;
        public final String alterEmoji;

        public FrontingInfo(String alterName, String alterEmoji) {
            this.alterName = alterName;
            this.alterEmoji = alterEmoji;
        }
    }
}
