# Body Customization - Visual Rendering TODO

## Current State (v0.1.6)
- ✅ Body customization GUI with drag sliders
- ✅ Settings save per alter (breast size, hip width, male bulge, body curves)
- ✅ Data persists across sessions in NBT format
- ✅ Player model preview shows in customization screen
- ❌ **Body changes don't visually render yet**

## What Needs to Happen

### High-Level Overview
To make body customization visually appear in-game, we need to:
1. Create custom player model renderers
2. Modify the player model geometry based on settings
3. Hook into Minecraft's rendering pipeline
4. Handle both first-person and third-person views

### Technical Requirements

#### 1. Custom Player Model Renderer
**File:** `client/renderer/CustomPlayerRenderer.java`

```java
public class CustomPlayerRenderer extends PlayerRenderer {
    // Override model creation to use our custom models
    // Modify vertex positions based on AlterProfile's BodyCustomization
}
```

**Approach Options:**
- **Option A (Forge Events):** Use `RenderPlayerEvent.Pre` to modify model
- **Option B (Custom Renderer):** Replace player renderer entirely
- **Option C (Mixins):** Use Mixin to inject into `PlayerRenderer`

**Recommended:** Option A (Forge Events) - Cleanest, no Mixins needed!

#### 2. Model Geometry Modification
**File:** `client/model/CustomPlayerModel.java`

```java
public class CustomPlayerModel extends PlayerModel<AbstractClientPlayer> {
    private BodyCustomization bodySettings;

    @Override
    public void setupAnim(...) {
        super.setupAnim(...);

        if (bodySettings != null && bodySettings.isCustomBodyEnabled()) {
            // Modify chest vertices based on breastSize
            adjustChestSize(bodySettings.getBreastSize());

            // Modify hip vertices based on hipWidth
            adjustHipWidth(bodySettings.getHipWidth());

            // Modify groin vertices based on maleBulge
            adjustMaleBulge(bodySettings.getMaleBulge());

            // Modify overall curves based on bodyCurves
            adjustBodyCurves(bodySettings.getBodyCurves());
        }
    }
}
```

**Key Challenges:**
- Minecraft's player model uses `HumanoidModel` with fixed geometry
- Need to scale specific `ModelPart` objects (body, arms, legs)
- Must preserve armor/clothing layer alignment
- Handle animation compatibility (walking, running, swimming)

#### 3. Player Model Part Modifications

**Chest (Breasts):**
```java
private void adjustChestSize(float size) {
    ModelPart body = this.body;

    // Scale chest area forward (Z-axis)
    float scale = 1.0f + (size * 0.5f); // Max 1.5x at 100%
    body.zScale = scale;

    // Adjust Y position to prevent clipping
    body.y -= (size * 2.0f);
}
```

**Hips:**
```java
private void adjustHipWidth(float width) {
    ModelPart body = this.body;
    ModelPart leftLeg = this.leftLeg;
    ModelPart rightLeg = this.rightLeg;

    // Widen hip area
    float scale = 1.0f + (width * 0.3f); // Max 1.3x at 100%
    body.xScale = scale;

    // Adjust leg positions
    leftLeg.x = -2.0f * scale;
    rightLeg.x = 2.0f * scale;
}
```

**Male Bulge:**
```java
private void adjustMaleBulge(float bulge) {
    ModelPart body = this.body;
    ModelPart leftLeg = this.leftLeg;
    ModelPart rightLeg = this.rightLeg;

    // Adjust groin area (lower body front)
    float offset = bulge * 1.5f;
    // Apply subtle Z-axis scaling to lower body front
    // This is the trickiest part!
}
```

**Body Curves:**
```java
private void adjustBodyCurves(float curves) {
    // This affects the transition between chest/waist/hips
    // Smoother transitions = more curves
    // Could use easing functions for interpolation
}
```

#### 4. Integration Points

**A. Render Event Hook (Recommended)**
```java
@SubscribeEvent
public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
    Player player = event.getEntity();
    UUID playerUUID = player.getUUID();

    // Get this player's system profile
    SystemProfile profile = SystemDataManager.getProfile(playerUUID);
    if (profile != null && profile.getCurrentAlter() != null) {
        AlterProfile alter = profile.getCurrentAlter();
        BodyCustomization body = alter.getBodyCustomization();

        if (body.isCustomBodyEnabled()) {
            // Get the player model from renderer
            PlayerModel model = (PlayerModel) event.getRenderer().getModel();

            // Apply body customizations
            applyBodyCustomization(model, body);
        }
    }
}
```

**B. Client Tick Handler**
```java
@SubscribeEvent
public static void onClientTick(TickEvent.ClientTickEvent event) {
    // Update body customization every tick
    // Ensures smooth transitions when switching alters
}
```

#### 5. Multi-Player Support

**Problem:** Other players need to see your body customizations!

**Solution:** Network packets to sync body data

**File:** `network/BodySyncPacket.java`
```java
public class BodySyncPacket {
    private UUID playerUUID;
    private BodyCustomization bodyData;

    // Send from client to server when switching alters
    // Server broadcasts to all other clients
}
```

**Packet Registration:**
```java
public class PacketHandler {
    public static void register() {
        // Register body sync packet
        // Send when alter switches
        // Receive and cache for other players
    }
}
```

#### 6. Performance Considerations

- **Cache modified models** - Don't recalculate every frame!
- **Only modify when needed** - Check if settings changed
- **Use LOD (Level of Detail)** - Reduce detail for distant players
- **Client-side only for self** - Server doesn't need to render

### Implementation Steps (Recommended Order)

1. **Phase 1: Basic Rendering** (v0.2.0)
   - [ ] Create `RenderPlayerEvent.Pre` handler
   - [ ] Get player's body customization settings
   - [ ] Apply simple scaling to chest (breasts only)
   - [ ] Test in singleplayer

2. **Phase 2: Full Body Mods** (v0.2.1)
   - [ ] Add hip width scaling
   - [ ] Add male bulge geometry
   - [ ] Add body curves smoothing
   - [ ] Handle armor layer alignment

3. **Phase 3: Networking** (v0.2.2)
   - [ ] Create body sync packets
   - [ ] Send on alter switch
   - [ ] Receive and apply to other players
   - [ ] Test in multiplayer

4. **Phase 4: Polish** (v0.2.3)
   - [ ] Smooth transitions when switching alters
   - [ ] Animation compatibility (swimming, crawling, elytra)
   - [ ] Performance optimization
   - [ ] Config for render distance

### Alternative: Wildfire Integration

Instead of implementing our own rendering, we could:
1. Detect Wildfire's Gender Mod
2. Set their settings programmatically when switching alters
3. Let them handle the rendering

**Pros:**
- No rendering code needed!
- Already battle-tested
- Community familiar with their rendering

**Cons:**
- Depends on their mod
- Can't add features they don't have (male bulge)
- Must maintain compatibility with their updates

### Resources Needed

- **3D Model Knowledge:** Understanding ModelPart, Pose, transforms
- **Rendering Pipeline:** How Minecraft renders players
- **Animation System:** How player animations work
- **Networking:** Packet handling for multiplayer

### Estimated Complexity

- **Time:** 10-20 hours for full implementation
- **Difficulty:** ⭐⭐⭐⭐⭐ (Advanced)
- **Testing Needs:** Extensive (all animations, armor types, multiplayer)

---

## Current Workaround

For now (v0.1.6), users can:
1. Adjust body settings in PluralCraft
2. Manually adjust Wildfire's mod to match
3. Switch between alters and update Wildfire manually

Future integration will automate this!

---

## Questions to Answer

1. **Do we implement our own rendering or integrate with Wildfire?**
   - Own: Full control, more work
   - Wildfire: Less work, dependency

2. **First-person arms?**
   - Should breasts be visible in first-person?
   - How does this affect mining/combat feel?

3. **Armor compatibility?**
   - How do we ensure armor/clothing scales properly?
   - Elytra, chest plates, etc.

4. **Animation edge cases?**
   - Swimming
   - Crawling
   - Elytra flying
   - Sleeping

---

**Next Steps:** Decide on approach, then start Phase 1! 🚀
