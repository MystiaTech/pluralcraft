package com.pluralcraft.data;

import net.minecraft.nbt.CompoundTag;

/**
 * Body customization settings for an alter
 * Includes features inspired by Wildfire's Gender Mod plus extras!
 * - Breast size (0-100)
 * - Hip width (0-100)
 * - Male bulge (0-100)
 * - Body curves (0-100)
 */
public class BodyCustomization {
    private float breastSize;      // 0.0 to 1.0 (0-100 scale)
    private float hipWidth;        // 0.0 to 1.0
    private float maleBulge;       // 0.0 to 1.0
    private float bodyCurves;      // 0.0 to 1.0 (overall body curviness)
    private boolean customBodyEnabled;

    public BodyCustomization() {
        // Default: no customization
        this.breastSize = 0.3f;     // Small default
        this.hipWidth = 0.5f;       // Neutral
        this.maleBulge = 0.0f;      // None by default
        this.bodyCurves = 0.5f;     // Neutral
        this.customBodyEnabled = false;
    }

    // Getters
    public float getBreastSize() { return breastSize; }
    public float getHipWidth() { return hipWidth; }
    public float getMaleBulge() { return maleBulge; }
    public float getBodyCurves() { return bodyCurves; }
    public boolean isCustomBodyEnabled() { return customBodyEnabled; }

    // Setters with validation (0.0 to 1.0)
    public void setBreastSize(float size) {
        this.breastSize = Math.max(0.0f, Math.min(1.0f, size));
    }

    public void setHipWidth(float width) {
        this.hipWidth = Math.max(0.0f, Math.min(1.0f, width));
    }

    public void setMaleBulge(float bulge) {
        this.maleBulge = Math.max(0.0f, Math.min(1.0f, bulge));
    }

    public void setBodyCurves(float curves) {
        this.bodyCurves = Math.max(0.0f, Math.min(1.0f, curves));
    }

    public void setCustomBodyEnabled(boolean enabled) {
        this.customBodyEnabled = enabled;
    }

    /**
     * Save to NBT
     */
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("breastSize", breastSize);
        tag.putFloat("hipWidth", hipWidth);
        tag.putFloat("maleBulge", maleBulge);
        tag.putFloat("bodyCurves", bodyCurves);
        tag.putBoolean("customBodyEnabled", customBodyEnabled);
        return tag;
    }

    /**
     * Load from NBT
     */
    public void loadFromNBT(CompoundTag tag) {
        this.breastSize = tag.getFloat("breastSize");
        this.hipWidth = tag.getFloat("hipWidth");
        this.maleBulge = tag.getFloat("maleBulge");
        this.bodyCurves = tag.getFloat("bodyCurves");
        this.customBodyEnabled = tag.getBoolean("customBodyEnabled");
    }

    /**
     * Create from NBT
     */
    public static BodyCustomization fromNBT(CompoundTag tag) {
        BodyCustomization body = new BodyCustomization();
        body.loadFromNBT(tag);
        return body;
    }

    /**
     * Clone this customization
     */
    public BodyCustomization copy() {
        BodyCustomization copy = new BodyCustomization();
        copy.breastSize = this.breastSize;
        copy.hipWidth = this.hipWidth;
        copy.maleBulge = this.maleBulge;
        copy.bodyCurves = this.bodyCurves;
        copy.customBodyEnabled = this.customBodyEnabled;
        return copy;
    }

    @Override
    public String toString() {
        if (!customBodyEnabled) {
            return "Default Body";
        }
        return String.format("Body(Breast:%.0f%%, Hips:%.0f%%, Bulge:%.0f%%, Curves:%.0f%%)",
                breastSize * 100, hipWidth * 100, maleBulge * 100, bodyCurves * 100);
    }
}
