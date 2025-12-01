# Skin Customization - Visual Rendering TODO

## Current State (v0.1.7)
- ✅ Skin customization GUI with URL input
- ✅ Settings save per alter (skin URL)
- ✅ Data persists across sessions in NBT format
- ✅ Player model preview shows in customization screen
- ✅ Skin loading system (SkinManager)
- ✅ Texture caching and management
- ✅ Auto-detection of alter switches
- ❌ **Skin changes don't visually render on player model yet**

## What Needs to Happen

### High-Level Overview
To make skin customization visually appear in-game, we need to:
1. Hook into Minecraft's player skin rendering system
2. Override the skin texture retrieval for the local player
3. Handle skin refresh when switching alters
4. Support both multiplayer (other players see your skin) and singleplayer

### Technical Requirements

#### 1. Player Skin Texture Override
**File:** `client/renderer/SkinTextureManager.java` (to create)

The challenge is that Minecraft's player rendering uses `PlayerInfo` to get skin textures from Mojang's servers. We need to intercept this!

**Approach Options:**
- **Option A (Mixin - Most Reliable):** Use Mixin to inject into `AbstractClientPlayer.getSkin()`
- **Option B (Render Event):** Hook `RenderPlayerEvent` and swap texture before rendering
- **Option C (Reflection):** Use reflection to modify PlayerInfo skin texture field

**Recommended:** Option B (Render Event) - No Mixins needed for Forge!

```java
@SubscribeEvent
public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
    Player player = event.getEntity();

    // Get custom skin from our SkinManager
    ResourceLocation customSkin = SkinManager.getCustomSkin(player.getUUID());

    if (customSkin != null) {
        // Swap the texture binding before rendering
        // This is the tricky part!
    }
}
```

#### 2. The Core Problem: Skin Texture Binding

Minecraft's player renderer gets the skin texture from:
```java
AbstractClientPlayer.getSkin() -> PlayerInfo.getSkin() -> Mojang servers
```

We need to override this chain. Here's how:

**Method 1: RenderPlayerEvent Texture Swap**
```java
@SubscribeEvent
public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
    Player player = event.getEntity();
    ResourceLocation customSkin = SkinManager.getCustomSkin(player.getUUID());

    if (customSkin != null) {
        // Get the renderer
        EntityRenderer<? extends Player> renderer = event.getRenderer();

        // We need to bind our texture BEFORE the model renders
        // But the renderer already grabbed the texture...
        // This is where it gets complex!
    }
}
```

**Method 2: Custom PlayerRenderer (More Work)**
```java
public class CustomPlayerRenderer extends PlayerRenderer {
    @Override
    public ResourceLocation getTextureLocation(AbstractClientPlayer player) {
        // Check if this player has a custom skin
        ResourceLocation customSkin = SkinManager.getCustomSkin(player.getUUID());
        if (customSkin != null) {
            return customSkin;
        }
        return super.getTextureLocation(player);
    }
}
```

Then register this custom renderer:
```java
@SubscribeEvent
public static void onRegisterRenderers(EntityRenderersEvent.AddLayers event) {
    // Replace default player renderer with ours
    EntityRendererProvider.Context context = ...;
    event.getSkin(PlayerRenderer.class).forEach((skin, renderer) -> {
        // Replace with custom renderer
    });
}
```

#### 3. What We've Already Done (v0.1.7)

✅ **SkinManager.java**
- Downloads skins from URLs
- Caches to disk (`pluralcraft/skins/`)
- Loads as NativeImage
- Registers as DynamicTexture
- Stores ResourceLocation in cachedSkins map

✅ **KeyInputHandler.java**
- Detects alter switches every client tick
- Calls `SkinManager.applySkin()` when alter changes

✅ **Data Storage**
- Skin URLs save per alter in NBT
- Persist across sessions

#### 4. What's Missing

❌ **Actually applying the texture to the player model**

The textures are loaded and registered, but we're not telling the player renderer to USE them!

#### 5. Implementation Steps (Recommended Order)

**Phase 1: Basic Skin Rendering (v0.2.0)**
1. [ ] Create RenderPlayerEvent.Pre handler
2. [ ] Get custom skin from SkinManager
3. [ ] Bind texture before rendering (research needed!)
4. [ ] Test in singleplayer

**Phase 2: Render System Integration (v0.2.1)**
5. [ ] Create custom PlayerRenderer extending PlayerRenderer
6. [ ] Override `getTextureLocation()` method
7. [ ] Register custom renderer on client init
8. [ ] Handle both slim/default model types

**Phase 3: Multiplayer Support (v0.2.2)**
9. [ ] Create skin sync packets
10. [ ] Send on alter switch (client → server → all clients)
11. [ ] Receive and cache for other players
12. [ ] Test in multiplayer

**Phase 4: Polish (v0.2.3)**
13. [ ] Smooth transitions when switching alters
14. [ ] Handle skin loading failures gracefully
15. [ ] Performance optimization (don't re-load every frame!)
16. [ ] Config for enabling/disabling feature

### Alternative: Fabric API Approach

Fabric has built-in APIs for custom skins! If we port to Fabric:
```java
PlayerRenderEvents.ALLOW_RENDER.register((player, renderer, tick, matrices, vertexConsumers, light) -> {
    // Swap texture here - much cleaner!
    return true;
});
```

This is one reason multi-loader support would be nice!

### Key Research Needed

1. **How to bind texture in RenderPlayerEvent?**
   - Need to research if we can swap the bound texture mid-render
   - Or if we need to replace the renderer entirely

2. **PlayerInfo texture field**
   - Can we use reflection to modify it?
   - Is it cached or fetched every frame?

3. **Existing mods that do this**
   - Look at CustomPlayerModels mod
   - Look at Skin Restorer mods
   - See how they solved this problem

### Resources Needed

- **Rendering Pipeline Knowledge:** How Minecraft renders players
- **Texture Binding:** How to bind custom textures
- **Event System:** Which events fire when
- **Reflection:** Modifying private fields if needed

### Estimated Complexity

- **Time:** 5-10 hours for basic implementation
- **Difficulty:** ⭐⭐⭐⭐ (Advanced - requires deep rendering knowledge)
- **Testing Needs:** Extensive (all views, multiplayer, edge cases)

---

## Current Workaround (v0.1.7)

For now, users can:
1. Set skin URLs in PluralCraft
2. Settings save properly
3. Switch alters and URLs update
4. **Manual workaround:** Manually change their Minecraft skin to match

Future implementation will make it automatic!

---

## Questions to Answer

1. **Can we swap textures in RenderPlayerEvent?**
   - Or do we need a custom renderer?

2. **First-person arms?**
   - Will custom skin show on first-person arms?
   - Does this work automatically or need special handling?

3. **Multiplayer sync?**
   - Do we need packets or can we use existing skin sync?
   - How do other players see custom skins?

4. **Mod compatibility?**
   - Will this work with Optifine/Iris shaders?
   - What about other player model mods?

---

**Next Steps:** Research existing skin mods, then implement Phase 1! 🚀
