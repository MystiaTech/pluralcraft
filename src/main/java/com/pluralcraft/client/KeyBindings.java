package com.pluralcraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pluralcraft.PluralCraft;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side keybindings for PluralCraft
 */
@Mod.EventBusSubscriber(modid = PluralCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class KeyBindings {

    // Key to open the alter management GUI
    public static final KeyMapping OPEN_GUI = new KeyMapping(
            "key.pluralcraft.open_gui",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P, // Default to 'P' key
            "key.categories.pluralcraft"
    );

    // Key to quick-switch to next alter
    public static final KeyMapping QUICK_SWITCH = new KeyMapping(
            "key.pluralcraft.quick_switch",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN, // Not bound by default
            "key.categories.pluralcraft"
    );

    // Key to open emoji picker (for nonverbal alters!)
    public static final KeyMapping OPEN_EMOJI_PICKER = new KeyMapping(
            "key.pluralcraft.open_emoji_picker",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_E, // Default to 'E' key (when not looking at a block)
            "key.categories.pluralcraft"
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_GUI);
        event.register(QUICK_SWITCH);
        event.register(OPEN_EMOJI_PICKER);
        PluralCraft.LOGGER.info("Registered PluralCraft keybindings!");
    }
}
