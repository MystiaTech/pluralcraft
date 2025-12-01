# Mod Integration Guide

PluralCraft supports saving and loading settings from other mods per alter! This means each alter can have their own body type, cosmetics, and more! 💜

## Supported Mods

### Currently Implemented:
- (Coming soon!)

### Planned Support:
- **Wildfire's Female Gender Mod** - Body customization (chest size, curves, etc.)
- **Cosmetic Armor** - Cosmetic armor slots
- More to come based on community requests!

## How It Works

When you switch alters, PluralCraft:
1. Saves your current mod settings to your current alter
2. Loads the new alter's saved mod settings
3. Applies those settings to the integrated mods

This happens automatically! No manual configuration needed!

## For Mod Developers

Want to add integration for your mod? Here's how!

### Step 1: Implement IModIntegration

Create a class that implements `IModIntegration`:

```java
package com.pluralcraft.integration.impl;

import com.pluralcraft.integration.IModIntegration;
import net.minecraft.nbt.CompoundTag;

public class YourModIntegration implements IModIntegration {

    @Override
    public CompoundTag captureCurrentSettings() {
        CompoundTag tag = new CompoundTag();

        // Read settings from your mod and save to NBT
        // Example:
        // tag.putFloat("someSetting", YourMod.getSomeSetting());

        return tag;
    }

    @Override
    public void applySettings(CompoundTag settings) {
        // Read settings from NBT and apply to your mod
        // Example:
        // if (settings.contains("someSetting")) {
        //     YourMod.setSomeSetting(settings.getFloat("someSetting"));
        // }
    }

    @Override
    public String getModId() {
        return "yourmodid";
    }

    @Override
    public String getDisplayName() {
        return "Your Mod Name";
    }
}
```

### Step 2: Register Your Integration

In `ModIntegration.java`, add your mod check:

```java
// Check for Your Mod
if (ModList.get().isLoaded("yourmodid")) {
    PluralCraft.LOGGER.info("Your Mod detected! Enabling integration.");
    INTEGRATIONS.put("yourmodid", new YourModIntegration());
}
```

### Step 3: Test!

1. Build PluralCraft
2. Load it with your mod
3. Create an alter
4. Switch alters - settings should save/load!

## Request Integration

Don't want to code it yourself? No problem!

Open an issue on GitHub with:
- Mod name and CurseForge/Modrinth link
- What settings should be saved per alter
- Why it's important for systems

We'll do our best to add support! 💜

## Technical Details

### Data Storage

Each alter profile stores mod settings in an NBT CompoundTag:

```java
alterProfile.setModSetting("yourmodid", settingsTag);
CompoundTag settings = alterProfile.getModSetting("yourmodid");
```

### When Settings Are Saved/Loaded

- **Saved**: When switching away from an alter
- **Loaded**: When switching to an alter
- **Manual**: Can be triggered via GUI (coming soon!)

### Error Handling

Integrations are wrapped in try-catch blocks. If one fails, others still work!

## Examples

### Example 1: Simple Boolean Setting

```java
@Override
public CompoundTag captureCurrentSettings() {
    CompoundTag tag = new CompoundTag();
    tag.putBoolean("enabled", YourMod.isEnabled());
    return tag;
}

@Override
public void applySettings(CompoundTag settings) {
    if (settings.contains("enabled")) {
        YourMod.setEnabled(settings.getBoolean("enabled"));
    }
}
```

### Example 2: Multiple Settings

```java
@Override
public CompoundTag captureCurrentSettings() {
    CompoundTag tag = new CompoundTag();
    tag.putFloat("size", YourMod.getSize());
    tag.putInt("color", YourMod.getColor());
    tag.putString("texture", YourMod.getTexture());
    return tag;
}

@Override
public void applySettings(CompoundTag settings) {
    if (settings.contains("size")) {
        YourMod.setSize(settings.getFloat("size"));
    }
    if (settings.contains("color")) {
        YourMod.setColor(settings.getInt("color"));
    }
    if (settings.contains("texture")) {
        YourMod.setTexture(settings.getString("texture"));
    }
}
```

## Questions?

Open an issue or discussion on GitHub! We're here to help! 💜
