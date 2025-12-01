package com.pluralcraft.emotes;

/**
 * Common emote actions for nonverbal communication
 */
public enum EmoteAction {
    // Positive reactions
    NOD("nods", "👍"),
    SMILE("smiles", "😊"),
    WAVE("waves", "👋"),
    THUMBS_UP("gives a thumbs up", "👍"),
    CLAP("claps", "👏"),

    // Negative reactions
    SHAKE_HEAD("shakes their head", "👎"),
    FROWN("frowns", "😞"),
    CRY("cries", "😢"),

    // Neutral/Communication
    SHRUG("shrugs", "🤷"),
    POINT("points", "👉"),
    LISTEN("listens carefully", "👂"),
    THINK("thinks", "🤔"),

    // Affection
    HUG("hugs", "🤗"),
    HEART("makes a heart gesture", "❤️"),
    PEACE("makes a peace sign", "✌️"),

    // Attention
    RAISE_HAND("raises their hand", "🙋"),
    LOOK_AWAY("looks away", "👀"),
    HIDE("hides", "🙈"),

    // Custom
    SIGN("signs something", "✍️"),
    GESTURE("makes a gesture", "🤲");

    private final String action;
    private final String emoji;

    EmoteAction(String action, String emoji) {
        this.action = action;
        this.emoji = emoji;
    }

    public String getAction() {
        return action;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDisplayName() {
        return emoji + " " + action;
    }
}
