package com.pluralcraft.client.gui.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

/**
 * Custom slider for body customization
 * Allows drag control with 1% precision
 */
public class BodySlider extends AbstractSliderButton {
    private final String label;
    private final ValueConsumer onValueChange;

    @FunctionalInterface
    public interface ValueConsumer {
        void accept(float value);
    }

    public BodySlider(int x, int y, int width, int height, String label, float initialValue, ValueConsumer onValueChange) {
        super(x, y, width, height, Component.literal(formatLabel(label, initialValue)), initialValue);
        this.label = label;
        this.onValueChange = onValueChange;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(formatLabel(label, (float)this.value)));
    }

    @Override
    protected void applyValue() {
        // Called when slider is dragged
        this.onValueChange.accept((float)this.value);
    }

    private static String formatLabel(String label, float value) {
        return label + ": " + (int)(value * 100) + "%";
    }

    /**
     * Set the slider value programmatically
     */
    public void setSliderValue(float value) {
        this.value = Math.max(0.0, Math.min(1.0, value));
        this.updateMessage();
    }
}
