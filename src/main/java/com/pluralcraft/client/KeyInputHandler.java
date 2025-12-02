package com.pluralcraft.client;

import com.pluralcraft.PluralCraft;
import com.pluralcraft.client.gui.AlterManagementScreen;
import com.pluralcraft.client.gui.EmojiPickerScreen;
import com.pluralcraft.client.skin.SkinManager;
import com.pluralcraft.compat.WildfireCompat;
import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
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

    private static int lastAlterIndex = -1;

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
                if (mc.player != null) {
                    SystemProfile profile = SystemDataManager.getProfile(mc.player.getUUID());
                    if (profile != null && !profile.getAlters().isEmpty()) {
                        // Switch to next alter
                        profile.switchToNextAlter();
                        SystemDataManager.saveData();

                        // Show message
                        AlterProfile newAlter = profile.getCurrentAlter();
                        if (newAlter != null) {
                            mc.player.sendSystemMessage(
                                net.minecraft.network.chat.Component.literal("Switched to: " + newAlter.getName()));

                            // Apply skin immediately
                            SkinManager.applySkin(newAlter);

                            // Apply body to Wildfire if loaded!
                            if (WildfireCompat.isWildfireLoaded()) {
                                boolean success = WildfireCompat.applyBodyToWildfire(mc.player, newAlter.getBodyCustomization());
                                if (success) {
                                    mc.player.sendSystemMessage(
                                        net.minecraft.network.chat.Component.literal("✓ Applied body settings to Wildfire!"));
                                }
                            }
                        }
                    } else {
                        mc.player.sendSystemMessage(
                            net.minecraft.network.chat.Component.literal("No alters to switch to!"));
                    }
                }
            }

            // Check if emoji picker key was pressed
            if (KeyBindings.OPEN_EMOJI_PICKER.consumeClick()) {
                if (mc.screen == null) { // Only open if no screen is already open
                    mc.setScreen(new EmojiPickerScreen());
                }
            }

            // Check if alter has changed and apply skin if so
            if (mc.player != null) {
                SystemProfile profile = SystemDataManager.getProfile(mc.player.getUUID());
                if (profile != null) {
                    int currentIndex = profile.getCurrentAlterIndex();
                    if (currentIndex != lastAlterIndex) {
                        lastAlterIndex = currentIndex;

                        // Alter switched! Apply the new skin
                        AlterProfile currentAlter = profile.getCurrentAlter();
                        if (currentAlter != null) {
                            SkinManager.applySkin(currentAlter);

                            // Apply body to Wildfire if loaded!
                            if (WildfireCompat.isWildfireLoaded()) {
                                WildfireCompat.applyBodyToWildfire(mc.player, currentAlter.getBodyCustomization());
                            }
                        }
                    }
                }
            }
        }
    }
}
