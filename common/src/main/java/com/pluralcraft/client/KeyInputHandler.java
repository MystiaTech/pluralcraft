package com.pluralcraft.client;

import com.pluralcraft.PluralCraft;
import com.pluralcraft.client.gui.AlterManagementScreen;
import com.pluralcraft.client.gui.EmojiPickerScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles key input for PluralCraft
 */
@Mod.EventBusSubscriber(modid = PluralCraft.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();

            // Check if open GUI key was pressed
            if (KeyBindings.OPEN_GUI.consumeClick()) {
                if (mc.screen == null) { // Only open if no screen is already open
                    mc.setScreen(new AlterManagementScreen(null));
                }
            }

            // Check if quick switch key was pressed
            if (KeyBindings.QUICK_SWITCH.consumeClick()) {
                // TODO: Implement quick switch
                if (mc.player != null) {
                    mc.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Quick switch pressed!"));
                }
            }

            // Check if emoji picker key was pressed
            if (KeyBindings.OPEN_EMOJI_PICKER.consumeClick()) {
                if (mc.screen == null) { // Only open if no screen is already open
                    mc.setScreen(new EmojiPickerScreen());
                }
            }
        }
    }
}
