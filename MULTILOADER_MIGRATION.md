# Multi-Loader Migration Guide

## 🎯 Goal
Unify PluralCraft to support **Forge**, **Fabric**, and **NeoForge** from a single codebase!

## 📁 New Structure

```
PluralCraft/
├── common/                   # 90% of code (shared)
│   ├── src/main/java/
│   │   └── com/pluralcraft/
│   │       ├── data/         # AlterProfile, SystemProfile, BodyCustomization
│   │       ├── client/       # GUIs (AlterManagementScreen, etc.)
│   │       ├── emotes/       # EmojiCategory
│   │       ├── items/        # SystemIDItem (loader-agnostic logic)
│   │       └── core/         # Core utilities
│   └── src/main/resources/
│       └── assets/pluralcraft/  # Textures, lang files, models
│
├── forge/                    # Forge-specific (10%)
│   ├── src/main/java/
│   │   └── com/pluralcraft/forge/
│   │       ├── PluralCraftForge.java      # Main mod class
│   │       ├── ForgeEventHandlers.java     # Forge events
│   │       └── client/
│   │           ├── ForgeKeyBindings.java   # Forge keybinding registration
│   │           └── ForgeClientSetup.java   # Client init
│   └── src/main/resources/
│       └── META-INF/mods.toml
│
├── neoforge/                 # NeoForge-specific (10%)
│   ├── src/main/java/
│   │   └── com/pluralcraft/neoforge/
│   │       ├── PluralCraftNeoForge.java
│   │       └── client/
│   └── src/main/resources/
│       └── META-INF/neoforge.mods.toml
│
└── fabric/                   # Fabric-specific (10%)
    ├── src/main/java/
    │   └── com/pluralcraft/fabric/
    │       ├── PluralCraftFabric.java
    │       └── client/
    │           └── FabricKeyBindings.java
    └── src/main/resources/
        └── fabric.mod.json
```

## 🔄 What Goes Where?

### Common Module (Loader-Agnostic)
**✅ Move here:**
- All data classes (`AlterProfile`, `SystemProfile`, `BodyCustomization`, `CommunicationMethod`)
- GUI screens (`AlterManagementScreen`, `AddAlterScreen`, `BodyCustomizationScreen`, `EmojiPickerScreen`)
- Emoji categories and logic
- Item logic (not registration!)
- All resources (textures, lang files, models)

**❌ Keep in loader modules:**
- Mod initialization (`@Mod`, `ModInitializer`)
- Event handlers (Forge events, Fabric events)
- Item/block registration
- Keybinding registration (different per loader)

### Forge Module
- `PluralCraftForge.java` - Main mod class with `@Mod` annotation
- `ForgeEventHandlers.java` - Forge-specific events
- `ForgeKeyBindings.java` - Forge keybinding registration
- `mods.toml` - Forge metadata

### Fabric Module
- `PluralCraftFabric.java` - Implements `ModInitializer`
- `FabricKeyBindings.java` - Fabric keybinding registration
- `fabric.mod.json` - Fabric metadata
- Uses Fabric API

### NeoForge Module
- `PluralCraftNeoForge.java` - Similar to Forge but for NeoForge
- Uses NeoForge API (very similar to Forge)
- `neoforge.mods.toml` - NeoForge metadata

## 🚀 Migration Steps

### Step 1: Create Common Module
1. Copy all data classes to `common/src/main/java/com/pluralcraft/data/`
2. Copy all GUI classes to `common/src/main/java/com/pluralcraft/client/gui/`
3. Copy emotes to `common/src/main/java/com/pluralcraft/emotes/`
4. Copy resources to `common/src/main/resources/`

### Step 2: Update Forge Module
1. Keep only Forge-specific code in `forge/`
2. Update imports to reference common module
3. Update build.gradle to depend on common

### Step 3: Create Fabric Module
1. Create Fabric entry point
2. Implement Fabric-specific registration
3. Use Fabric API for keybindings

### Step 4: Create NeoForge Module
1. Similar to Forge (95% same)
2. Update to NeoForge APIs
3. Different metadata file

## 📦 Build System

### Root build.gradle
```gradle
allprojects {
    group = 'com.pluralcraft'
    version = '0.1.4'
}

subprojects {
    apply plugin: 'java'

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

### Forge build.gradle
```gradle
dependencies {
    implementation project(':common')
    minecraft "net.minecraftforge:forge:1.20.1-47.3.0"
}
```

### Fabric build.gradle
```gradle
dependencies {
    implementation project(':common')
    modImplementation "net.fabricmc:fabric-loader:0.15.0"
    modImplementation "net.fabricmc.fabric-api:fabric-api:0.90.0+1.20.1"
}
```

### NeoForge build.gradle
```gradle
dependencies {
    implementation project(':common')
    minecraft "net.neoforged:neoforge:1.20.1-47.3.0"
}
```

## 🎯 Key Differences Between Loaders

### Event Registration
- **Forge**: `@SubscribeEvent` with `@Mod.EventBusSubscriber`
- **Fabric**: `ServerLifecycleEvents.SERVER_STARTING.register(...)`
- **NeoForge**: Same as Forge (but different package names)

### Keybindings
- **Forge**: `KeyMapping` with `RegisterKeyMappingsEvent`
- **Fabric**: `KeyBindingHelper.registerKeyBinding()`
- **NeoForge**: Same as Forge

### Item Registration
- **Forge**: `DeferredRegister<Item>`
- **Fabric**: `Registry.register(Registries.ITEM, ...)`
- **NeoForge**: `DeferredRegister<Item>` (similar to Forge)

## ✅ Testing Plan
1. **Forge**: Test with existing test pack (current workflow)
2. **Fabric**: Create Fabric test instance
3. **NeoForge**: Create NeoForge test instance

## 📝 Current Status
- ✅ Directory structure created
- ⏳ Migration in progress
- ⏳ Common module population
- ⏳ Loader-specific modules

## 🎉 Benefits
- **Single codebase** - Update once, build three times!
- **Easier maintenance** - Bug fixes apply to all loaders
- **Consistent features** - All loaders get same features
- **Faster development** - No duplicate work!

---

**Next:** Start moving shared code to common module! 🚀
