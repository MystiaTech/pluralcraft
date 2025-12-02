package com.pluralcraft.client;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.BodyCustomization;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles rendering of per-alter body customizations
 * This OVERRIDES other mods (MCA, Wildfire) to apply the current alter's body settings
 */
@Mod.EventBusSubscriber(modid = "pluralcraft", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerBodyRenderer {

    /**
     * Called BEFORE the player is rendered
     * We use this to modify the player model based on the current alter's body customization
     */
    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();

        // Only modify the local player's model
        if (!(player instanceof AbstractClientPlayer)) {
            return;
        }

        SystemProfile profile = SystemDataManager.getProfile(player.getUUID());
        if (profile == null || profile.getCurrentAlter() == null) {
            return;
        }

        AlterProfile currentAlter = profile.getCurrentAlter();
        BodyCustomization body = currentAlter.getBodyCustomization();

        // Get the player model from the renderer
        PlayerModel<?> model = event.getRenderer().getModel();

        if (body.isCustomBodyEnabled()) {
            // Apply our custom body settings (OVERRIDE other mods!)
            applyBodyCustomization(model, body);
        } else {
            // Reset to vanilla defaults (OVERRIDE other mods!)
            resetToVanilla(model);
        }
    }

    /**
     * Apply body customization to the player model
     * This modifies the model's geometry based on the alter's settings
     */
    private static void applyBodyCustomization(PlayerModel<?> model, BodyCustomization body) {
        // Get body parts
        ModelPart bodyPart = model.body;
        ModelPart leftArm = model.leftArm;
        ModelPart rightArm = model.rightArm;
        ModelPart leftLeg = model.leftLeg;
        ModelPart rightLeg = model.rightLeg;

        // Apply breast size (modify body and arm positioning)
        float breastSize = body.getBreastSize();
        if (breastSize > 0) {
            // Increase body depth slightly
            float bodyScale = 1.0f + (breastSize * 0.3f);
            bodyPart.xScale = bodyScale;
            bodyPart.zScale = bodyScale;

            // Adjust arm positioning to account for breasts
            float armOffset = breastSize * 0.1f;
            leftArm.x += armOffset;
            rightArm.x -= armOffset;
        }

        // Apply hip width (modify leg positioning)
        float hipWidth = body.getHipWidth();
        if (hipWidth > 0) {
            float legOffset = hipWidth * 0.5f;
            leftLeg.x += legOffset;
            rightLeg.x -= legOffset;
        }

        // Apply body curves (overall body scaling)
        float curves = body.getBodyCurves();
        if (curves != 0.5f) { // 0.5 is neutral
            float curveScale = 0.8f + (curves * 0.4f); // Range: 0.8 to 1.2
            bodyPart.yScale = curveScale;
        }

        // Male bulge (if needed - modify leg geometry slightly)
        float bulge = body.getMaleBulge();
        if (bulge > 0) {
            float bulgeScale = 1.0f + (bulge * 0.2f);
            leftLeg.zScale = bulgeScale;
            rightLeg.zScale = bulgeScale;
        }
    }

    /**
     * Reset the player model to vanilla defaults
     * This OVERRIDES any modifications from other mods (MCA, Wildfire)
     */
    private static void resetToVanilla(PlayerModel<?> model) {
        // Get body parts
        ModelPart bodyPart = model.body;
        ModelPart leftArm = model.leftArm;
        ModelPart rightArm = model.rightArm;
        ModelPart leftLeg = model.leftLeg;
        ModelPart rightLeg = model.rightLeg;

        // Reset all scales to 1.0 (vanilla)
        bodyPart.xScale = 1.0f;
        bodyPart.yScale = 1.0f;
        bodyPart.zScale = 1.0f;

        leftArm.xScale = 1.0f;
        leftArm.yScale = 1.0f;
        leftArm.zScale = 1.0f;

        rightArm.xScale = 1.0f;
        rightArm.yScale = 1.0f;
        rightArm.zScale = 1.0f;

        leftLeg.xScale = 1.0f;
        leftLeg.yScale = 1.0f;
        leftLeg.zScale = 1.0f;

        rightLeg.xScale = 1.0f;
        rightLeg.yScale = 1.0f;
        rightLeg.zScale = 1.0f;

        // Reset positions (vanilla defaults)
        leftArm.x = 5.0f;
        rightArm.x = -5.0f;
        leftLeg.x = 1.9f;
        rightLeg.x = -1.9f;
    }
}
