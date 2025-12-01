package com.pluralcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pluralcraft.client.gui.widgets.BodySlider;
import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.BodyCustomization;
import com.pluralcraft.data.SystemDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

/**
 * GUI for customizing an alter's body appearance
 * Now with PROPER drag sliders and model preview!
 */
public class BodyCustomizationScreen extends Screen {
    private final Screen parentScreen;
    private final AlterProfile alter;
    private final BodyCustomization bodyCustomization;

    // Real sliders!
    private BodySlider breastSlider;
    private BodySlider hipSlider;
    private BodySlider bulgeSlider;
    private BodySlider curvesSlider;

    private Checkbox enableCustomBodyCheckbox;

    private static final int SLIDER_WIDTH = 200;
    private static final int SLIDER_HEIGHT = 20;

    // Model preview position
    private int modelX;
    private int modelY;
    private float modelRotation = 0f;

    public BodyCustomizationScreen(Screen parentScreen, AlterProfile alter) {
        super(Component.literal("Body Customization"));
        this.parentScreen = parentScreen;
        this.alter = alter;
        this.bodyCustomization = alter.getBodyCustomization();
    }

    @Override
    protected void init() {
        super.init();

        // Left side: Sliders
        int leftX = 30;
        int startY = 60;
        int spacing = 30;

        // Right side: Model preview
        this.modelX = this.width - 80;
        this.modelY = this.height / 2 + 30;

        // Enable/Disable custom body
        this.enableCustomBodyCheckbox = this.addRenderableWidget(
            new Checkbox(leftX, startY, 200, 20,
                Component.literal("Enable Custom Body"),
                bodyCustomization.isCustomBodyEnabled())
        );

        startY += spacing + 10;

        // Breast size slider - REAL DRAG SLIDER!
        this.breastSlider = this.addRenderableWidget(
            new BodySlider(leftX, startY, SLIDER_WIDTH, SLIDER_HEIGHT,
                "Breast Size",
                bodyCustomization.getBreastSize(),
                value -> bodyCustomization.setBreastSize(value))
        );

        startY += spacing;

        // Hip width slider
        this.hipSlider = this.addRenderableWidget(
            new BodySlider(leftX, startY, SLIDER_WIDTH, SLIDER_HEIGHT,
                "Hip Width",
                bodyCustomization.getHipWidth(),
                value -> bodyCustomization.setHipWidth(value))
        );

        startY += spacing;

        // Male bulge slider
        this.bulgeSlider = this.addRenderableWidget(
            new BodySlider(leftX, startY, SLIDER_WIDTH, SLIDER_HEIGHT,
                "Male Bulge",
                bodyCustomization.getMaleBulge(),
                value -> bodyCustomization.setMaleBulge(value))
        );

        startY += spacing;

        // Body curves slider
        this.curvesSlider = this.addRenderableWidget(
            new BodySlider(leftX, startY, SLIDER_WIDTH, SLIDER_HEIGHT,
                "Body Curves",
                bodyCustomization.getBodyCurves(),
                value -> bodyCustomization.setBodyCurves(value))
        );

        // Done button
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> {
                    // Save the enabled state
                    bodyCustomization.setCustomBodyEnabled(enableCustomBodyCheckbox.selected());
                    // Go back to parent screen
                    Minecraft.getInstance().setScreen(parentScreen);
                }
        ).bounds(leftX, this.height - 40, 95, 20).build());

        // Reset to defaults button
        this.addRenderableWidget(Button.builder(
                Component.literal("Reset"),
                button -> {
                    bodyCustomization.setBreastSize(0.3f);
                    bodyCustomization.setHipWidth(0.5f);
                    bodyCustomization.setMaleBulge(0.0f);
                    bodyCustomization.setBodyCurves(0.5f);
                    updateSliderValues();
                }
        ).bounds(leftX + 105, this.height - 40, 95, 20).build());
    }

    private void updateSliderValues() {
        breastSlider.setSliderValue(bodyCustomization.getBreastSize());
        hipSlider.setSliderValue(bodyCustomization.getHipWidth());
        bulgeSlider.setSliderValue(bodyCustomization.getMaleBulge());
        curvesSlider.setSliderValue(bodyCustomization.getBodyCurves());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw subtitle
        graphics.drawCenteredString(this.font,
                "Customizing: " + alter.getName(),
                this.width / 2, 35, 0xAAAAAA);

        // Draw info text (with proper padding from checkbox)
        int infoY = 105; // More padding from the checkbox area
        if (!enableCustomBodyCheckbox.selected()) {
            graphics.drawString(this.font,
                    "Enable custom body to use these settings",
                    30, infoY, 0xFF5555);
        } else {
            graphics.drawString(this.font,
                    "Drag sliders to adjust (1% precision)",
                    30, infoY, 0x55FF55);
        }

        // Draw model preview label
        graphics.drawString(this.font, "Preview:", this.width - 140, 60, 0xFFFFFF);

        // Draw player model preview
        if (minecraft != null && minecraft.player != null) {
            try {
                // Update rotation for spin effect
                modelRotation += partialTick * 2.0f;

                // Draw the player model (correct signature for 1.20.1)
                InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    this.modelX,
                    this.modelY,
                    40, // size
                    (float)(this.modelX - mouseX),
                    (float)(this.modelY - 50 - mouseY),
                    minecraft.player
                );
            } catch (Exception e) {
                // If rendering fails, just show a message
                graphics.drawString(this.font, "Model", this.width - 110, this.height / 2, 0xAAAAAA);
            }
        }

        // Draw note about rendering
        graphics.drawCenteredString(this.font,
                "Note: Visual changes coming in next update!",
                this.width / 2, this.height - 60, 0xAAAA00);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        // Save when closing
        bodyCustomization.setCustomBodyEnabled(enableCustomBodyCheckbox.selected());
        SystemDataManager.saveData();
        Minecraft.getInstance().setScreen(parentScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
