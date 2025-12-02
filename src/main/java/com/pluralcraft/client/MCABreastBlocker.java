package com.pluralcraft.client;

import com.pluralcraft.PluralCraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Blocks MCA Reborn's breast rendering layer from adding extra breasts
 * This prevents the "double breasts" issue when using Wildfire's Gender Mod
 */
@Mod.EventBusSubscriber(modid = PluralCraft.MOD_ID, value = Dist.CLIENT)
public class MCABreastBlocker {
    private static boolean hasTried = false;
    private static boolean mcaDetected = false;

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (!hasTried) {
            hasTried = true;
            mcaDetected = ModList.get().isLoaded("mca");

            if (mcaDetected) {
                System.out.println("[PluralCraft] MCA Reborn detected! Attempting to remove breast render layers...");
                removeRenderLayers(event.getRenderer());
            }
        }
    }

    private static void removeRenderLayers(PlayerRenderer renderer) {
        try {
            // Try to access the layers field using reflection
            // Minecraft's EntityRenderer has a 'layers' field
            Field layersField = null;

            // Try different possible field names
            String[] fieldNames = {"layers", "f_115290_", "field_177097_h"};

            for (String fieldName : fieldNames) {
                try {
                    layersField = renderer.getClass().getSuperclass().getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    // Try next field name
                }
            }

            if (layersField == null) {
                System.err.println("[PluralCraft] Could not find layers field in player renderer");
                return;
            }

            layersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<RenderLayer<Player, PlayerModel<Player>>> layers =
                (List<RenderLayer<Player, PlayerModel<Player>>>) layersField.get(renderer);

            // Remove any MCA breast/body layers
            int removedCount = 0;
            layers.removeIf(layer -> {
                String className = layer.getClass().getName();
                boolean isMCALayer = className.contains("mca") &&
                    (className.contains("Breast") || className.contains("Body") ||
                     className.contains("breast") || className.contains("body"));

                if (isMCALayer) {
                    System.out.println("[PluralCraft] ✓ Removed MCA layer: " + className);
                }

                return isMCALayer;
            });

            // Count how many layers we removed
            if (removedCount > 0) {
                System.out.println("[PluralCraft] Successfully removed MCA render layers!");
            } else {
                System.out.println("[PluralCraft] No MCA breast layers found to remove (might be integrated differently)");
            }

        } catch (Exception e) {
            System.err.println("[PluralCraft] Failed to remove MCA render layers: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
