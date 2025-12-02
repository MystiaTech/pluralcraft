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
        // Namespace our tags to avoid conflicts with Wildfire's Gender Mod!
        tag.putFloat("pluralcraft_breastSize", breastSize);
        tag.putFloat("pluralcraft_hipWidth", hipWidth);
        tag.putFloat("pluralcraft_maleBulge", maleBulge);
        tag.putFloat("pluralcraft_bodyCurves", bodyCurves);
        tag.putBoolean("pluralcraft_customBodyEnabled", customBodyEnabled);
        return tag;
    }

    /**
     * Load from NBT
     */
    public void loadFromNBT(CompoundTag tag) {
        // Try new namespaced tags first, fallback to old tags for backwards compatibility
        if (tag.contains("pluralcraft_breastSize")) {
            this.breastSize = tag.getFloat("pluralcraft_breastSize");
            this.hipWidth = tag.getFloat("pluralcraft_hipWidth");
            this.maleBulge = tag.getFloat("pluralcraft_maleBulge");
            this.bodyCurves = tag.getFloat("pluralcraft_bodyCurves");
            this.customBodyEnabled = tag.getBoolean("pluralcraft_customBodyEnabled");
        } else {
            // Backwards compatibility with old saves
            this.breastSize = tag.getFloat("breastSize");
            this.hipWidth = tag.getFloat("hipWidth");
            this.maleBulge = tag.getFloat("maleBulge");
            this.bodyCurves = tag.getFloat("bodyCurves");
            this.customBodyEnabled = tag.getBoolean("customBodyEnabled");
        }
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
