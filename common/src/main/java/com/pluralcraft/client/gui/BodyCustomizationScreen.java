package com.pluralcraft.client.gui;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.BodyCustomization;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * GUI for customizing an alter's body appearance
 * Features inspired by Wildfire's Gender Mod + extras!
 */
public class BodyCustomizationScreen extends Screen {
    private final Screen parentScreen;
    private final AlterProfile alter;
    private final BodyCustomization bodyCustomization;

    // Sliders (we'll use buttons for now, proper sliders in next update)
    private Button breastSlider;
    private Button hipSlider;
    private Button bulgeSlider;
    private Button curvesSlider;

    private Checkbox enableCustomBodyCheckbox;

    private static final int SLIDER_WIDTH = 200;
    private static final int SLIDER_HEIGHT = 20;

    public BodyCustomizationScreen(Screen parentScreen, AlterProfile alter) {
        super(Component.literal("Body Customization"));
        this.parentScreen = parentScreen;
        this.alter = alter;
        this.bodyCustomization = alter.getBodyCustomization();
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = 60;
        int spacing = 30;

        // Enable/Disable custom body
        this.enableCustomBodyCheckbox = this.addRenderableWidget(
            new Checkbox(centerX - 100, startY, 200, 20,
                Component.literal("Enable Custom Body"),
                bodyCustomization.isCustomBodyEnabled())
        );

        startY += spacing + 10;

        // Breast size slider
        this.breastSlider = this.addRenderableWidget(Button.builder(
                getSliderText("Breast Size", bodyCustomization.getBreastSize()),
                button -> {
                    // Cycle through values
                    float current = bodyCustomization.getBreastSize();
                    current += 0.1f;
                    if (current > 1.0f) current = 0.0f;
                    bodyCustomization.setBreastSize(current);
                    button.setMessage(getSliderText("Breast Size", bodyCustomization.getBreastSize()));
                }
        ).bounds(centerX - SLIDER_WIDTH / 2, startY, SLIDER_WIDTH, SLIDER_HEIGHT).build());

        startY += spacing;

        // Hip width slider
        this.hipSlider = this.addRenderableWidget(Button.builder(
                getSliderText("Hip Width", bodyCustomization.getHipWidth()),
                button -> {
                    float current = bodyCustomization.getHipWidth();
                    current += 0.1f;
                    if (current > 1.0f) current = 0.0f;
                    bodyCustomization.setHipWidth(current);
                    button.setMessage(getSliderText("Hip Width", bodyCustomization.getHipWidth()));
                }
        ).bounds(centerX - SLIDER_WIDTH / 2, startY, SLIDER_WIDTH, SLIDER_HEIGHT).build());

        startY += spacing;

        // Male bulge slider
        this.bulgeSlider = this.addRenderableWidget(Button.builder(
                getSliderText("Male Bulge", bodyCustomization.getMaleBulge()),
                button -> {
                    float current = bodyCustomization.getMaleBulge();
                    current += 0.1f;
                    if (current > 1.0f) current = 0.0f;
                    bodyCustomization.setMaleBulge(current);
                    button.setMessage(getSliderText("Male Bulge", bodyCustomization.getMaleBulge()));
                }
        ).bounds(centerX - SLIDER_WIDTH / 2, startY, SLIDER_WIDTH, SLIDER_HEIGHT).build());

        startY += spacing;

        // Body curves slider
        this.curvesSlider = this.addRenderableWidget(Button.builder(
                getSliderText("Body Curves", bodyCustomization.getBodyCurves()),
                button -> {
                    float current = bodyCustomization.getBodyCurves();
                    current += 0.1f;
                    if (current > 1.0f) current = 0.0f;
                    bodyCustomization.setBodyCurves(current);
                    button.setMessage(getSliderText("Body Curves", bodyCustomization.getBodyCurves()));
                }
        ).bounds(centerX - SLIDER_WIDTH / 2, startY, SLIDER_WIDTH, SLIDER_HEIGHT).build());

        // Done button
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> {
                    // Save the enabled state
                    bodyCustomization.setCustomBodyEnabled(enableCustomBodyCheckbox.selected());
                    // Go back to parent screen
                    Minecraft.getInstance().setScreen(parentScreen);
                }
        ).bounds(centerX - 100, this.height - 40, 200, 20).build());

        // Reset to defaults button
        this.addRenderableWidget(Button.builder(
                Component.literal("Reset to Defaults"),
                button -> {
                    bodyCustomization.setBreastSize(0.3f);
                    bodyCustomization.setHipWidth(0.5f);
                    bodyCustomization.setMaleBulge(0.0f);
                    bodyCustomization.setBodyCurves(0.5f);
                    updateSliderLabels();
                }
        ).bounds(centerX - 100, this.height - 65, 200, 20).build());
    }

    private Component getSliderText(String label, float value) {
        return Component.literal(label + ": " + (int)(value * 100) + "%");
    }

    private void updateSliderLabels() {
        breastSlider.setMessage(getSliderText("Breast Size", bodyCustomization.getBreastSize()));
        hipSlider.setMessage(getSliderText("Hip Width", bodyCustomization.getHipWidth()));
        bulgeSlider.setMessage(getSliderText("Male Bulge", bodyCustomization.getMaleBulge()));
        curvesSlider.setMessage(getSliderText("Body Curves", bodyCustomization.getBodyCurves()));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw subtitle
        graphics.drawCenteredString(this.font,
                "Customize " + alter.getName() + "'s body appearance",
                this.width / 2, 35, 0xAAAAAA);

        // Draw info text
        if (!enableCustomBodyCheckbox.selected()) {
            graphics.drawCenteredString(this.font,
                    "Enable custom body to use these settings",
                    this.width / 2, 95, 0xFF5555);
        } else {
            graphics.drawCenteredString(this.font,
                    "Click sliders to adjust values",
                    this.width / 2, 95, 0x55FF55);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        // Save when closing
        bodyCustomization.setCustomBodyEnabled(enableCustomBodyCheckbox.selected());
        Minecraft.getInstance().setScreen(parentScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
