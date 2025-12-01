# PluralCraft - Future Features TODO

This document tracks all planned features and improvements for PluralCraft.

## Version 0.2.x - Rendering & Visual Features

### Body Rendering (v0.2.0-0.2.3)
See [RENDERING_TODO.md](RENDERING_TODO.md) for detailed implementation plan.

**Status:** Not Started
**Priority:** High
**Complexity:** ⭐⭐⭐⭐⭐ (Very Advanced)

- [ ] Create custom player model renderer
- [ ] Modify player model geometry based on settings
- [ ] Hook into Minecraft's rendering pipeline
- [ ] Handle first-person and third-person views
- [ ] Support animations (swimming, crawling, elytra)
- [ ] Ensure armor compatibility
- [ ] Multiplayer networking for body sync
- [ ] Performance optimization

### Skin Rendering (v0.2.0-0.2.3)
See [SKIN_RENDERING_TODO.md](SKIN_RENDERING_TODO.md) for detailed implementation plan.

**Status:** Partially Complete (GUI + data system done)
**Priority:** High
**Complexity:** ⭐⭐⭐⭐ (Advanced)

- [x] GUI for skin customization
- [x] Data storage per alter
- [x] Texture loading and caching
- [x] Auto-detection of alter switches
- [ ] Hook into player skin rendering system
- [ ] Override skin texture retrieval
- [ ] Handle skin refresh on alter switch
- [ ] Multiplayer support (other players see your skin)
- [ ] File upload implementation (currently placeholder)

---

## Version 0.3.x - GUI Improvements

### Edit Details Screen (v0.3.0)
**File:** EditAlterScreen.java (currently shows placeholder)
**Status:** Not Started
**Priority:** Medium
**Complexity:** ⭐⭐ (Medium)

- [ ] Create EditDetailsScreen.java
- [ ] Edit fields for name, age, pronouns, bio
- [ ] Validation and error handling
- [ ] Save changes to SystemDataManager
- [ ] Hook up button in EditAlterScreen

**Location:** `EditAlterScreen.java:73` - "Edit Details" button

### Communication Method Editor (v0.3.0)
**File:** EditAlterScreen.java (currently shows placeholder)
**Status:** Not Started
**Priority:** Low
**Complexity:** ⭐⭐ (Medium)

- [ ] Create CommunicationMethodScreen.java
- [ ] Radio buttons or dropdown for method selection
- [ ] Save changes to alter profile
- [ ] Hook up button in EditAlterScreen

**Location:** `EditAlterScreen.java:85` - "Communication" button

### System Name Editor (v0.3.0)
**File:** AlterManagementScreen.java (currently shows placeholder)
**Status:** Not Started
**Priority:** Medium
**Complexity:** ⭐ (Easy)

- [ ] Create EditSystemNameScreen.java
- [ ] Text field for system name
- [ ] Validation
- [ ] Save to SystemProfile
- [ ] Hook up button in AlterManagementScreen

**Location:** `AlterManagementScreen.java:47` - "System:" button

### Current Alter Details Screen (v0.3.1)
**File:** AlterManagementScreen.java (currently shows placeholder)
**Status:** Not Started
**Priority:** Low
**Complexity:** ⭐ (Easy)

- [ ] Create AlterDetailsScreen.java
- [ ] Display all alter info (read-only or editable?)
- [ ] Link to Edit Details, Body Customization, Skin Customization
- [ ] Hook up button in AlterManagementScreen

**Location:** `AlterManagementScreen.java:56` - "Current:" button

---

## Version 0.4.x - Integration Features

### Wildfire's Gender Mod Integration (v0.4.0)
**Files:** EditAlterScreen.java, AddAlterScreen.java
**Status:** Detection done, API integration pending
**Priority:** Medium
**Complexity:** ⭐⭐⭐ (Depends on their API)

- [x] Detect if Wildfire's mod is loaded
- [x] Show Wildfire button when present
- [ ] Research Wildfire's API for opening their GUI
- [ ] Open Wildfire's screen from our button
- [ ] (Optional) Sync our body settings to theirs when both present

**Locations:**
- `EditAlterScreen.java:50` - "Wildfire's Gender Mod" button
- `AddAlterScreen.java:88` - "Wildfire's Gender Mod" button

### PronounDB Integration (v0.4.1)
**Status:** Not Started
**Priority:** Low
**Complexity:** ⭐⭐⭐ (API integration)

- [ ] Research PronounDB API
- [ ] Fetch pronouns from PronounDB
- [ ] Auto-populate pronoun field when creating alters
- [ ] Cache pronoun data
- [ ] Respect user privacy settings

### Cosmetic Armor Integration (v0.4.2)
**Status:** Not Started
**Priority:** Low
**Complexity:** ⭐⭐⭐ (Mod integration)

- [ ] Detect Cosmetic Armor mod
- [ ] Store cosmetic armor per alter
- [ ] Apply cosmetic armor on alter switch
- [ ] GUI for managing cosmetic armor per alter

---

## Version 0.5.x - Keybindings & Quick Actions

### Quick Switch Implementation (v0.5.0)
**File:** KeyInputHandler.java (currently shows placeholder)
**Status:** Keybinding registered, functionality pending
**Priority:** Medium
**Complexity:** ⭐⭐ (Medium)

- [x] Keybinding registered (unbound by default)
- [ ] Implement quick switch logic
- [ ] Switch to next alter in list
- [ ] Save data after switch
- [ ] Apply skin/body settings immediately
- [ ] Show notification message

**Location:** `KeyInputHandler.java:38` - Quick switch handler

### Keybinding Customization (v0.5.1)
**Status:** Works (Minecraft's built-in)
**Priority:** ✅ Complete
**Complexity:** N/A

Users can already customize keybindings in Minecraft's controls menu!

---

## Version 0.6.x - Chat & Communication

### Chat Formatting (v0.6.0)
**Status:** Not Started
**Priority:** Medium
**Complexity:** ⭐⭐⭐ (Advanced)

- [ ] Prefix chat messages with system name
- [ ] Show current alter name in chat
- [ ] Color coding per alter (optional)
- [ ] Format: `[SystemName | AlterName] message`
- [ ] Config option to enable/disable

### Chat Commands (v0.6.1)
**Status:** Basic commands done, advanced pending
**Priority:** Low
**Complexity:** ⭐⭐ (Medium)

Current commands:
- ✅ `/pluralcraft system setname <name>`
- ✅ `/pluralcraft alter add <name> <age> <pronouns>`
- ✅ `/pluralcraft alter switch <name>`
- ✅ `/pluralcraft alter list`

Planned commands:
- [ ] `/pluralcraft alter remove <name>`
- [ ] `/pluralcraft alter info <name>`
- [ ] `/pluralcraft system info`
- [ ] `/pluralcraft export` (export system data)
- [ ] `/pluralcraft import` (import system data)

---

## Version 0.7.x - File Management

### Skin File Upload (v0.7.0)
**File:** SkinCustomizationScreen.java
**Status:** Button exists, implementation pending
**Priority:** Medium
**Complexity:** ⭐⭐⭐ (Medium-Hard)

- [x] Button in GUI
- [ ] Implement file picker dialog
- [ ] Validate PNG format
- [ ] Check dimensions (64x64 or 64x32)
- [ ] Copy to cache directory
- [ ] Store as `file://` URL in AlterProfile
- [ ] Use SkinManager.loadSkinFromFile()

**Location:** `SkinCustomizationScreen.java:83` - Upload file button

**Technical Notes:**
- Use Java's JFileChooser or native file dialog
- Or create custom Minecraft screen for file selection
- Validate skin format (64x64, 64x32, 32x32 legacy)

---

## Version 0.8.x - Multi-Loader Support

### Fabric Port (v0.8.0)
**Status:** Architecture planned, not started
**Priority:** High (for wider adoption)
**Complexity:** ⭐⭐⭐⭐ (Advanced)

See [MULTILOADER_MIGRATION.md](MULTILOADER_MIGRATION.md) for detailed plan.

- [ ] Set up Fabric module
- [ ] Port common code
- [ ] Replace Forge-specific code with Fabric APIs
- [ ] Test all features on Fabric
- [ ] Set up CI/CD for both loaders

### NeoForge Port (v0.8.1)
**Status:** Architecture planned, not started
**Priority:** Medium
**Complexity:** ⭐⭐⭐ (Medium-Advanced)

- [ ] Set up NeoForge module
- [ ] Port Forge code to NeoForge
- [ ] Update to NeoForge APIs
- [ ] Test all features on NeoForge
- [ ] Set up CI/CD for NeoForge

---

## Version 0.9.x - Polish & Optimization

### Performance Optimization (v0.9.0)
**Status:** Not Critical Yet
**Priority:** Low (until we have rendering)
**Complexity:** ⭐⭐⭐ (Medium-Advanced)

- [ ] Profile mod performance
- [ ] Optimize skin loading (async, caching)
- [ ] Optimize body rendering (LOD system)
- [ ] Reduce tick handler overhead
- [ ] Optimize data serialization

### Configuration File (v0.9.1)
**Status:** Not Started
**Priority:** Low
**Complexity:** ⭐⭐ (Medium)

- [ ] Create config file (TOML format)
- [ ] Config options:
  - Enable/disable features
  - Chat formatting options
  - Keybinding defaults
  - Performance settings
- [ ] In-game config screen (optional)

### Testing & Bug Fixes (v0.9.2)
**Status:** Ongoing
**Priority:** High
**Complexity:** Varies

- [ ] Test in singleplayer
- [ ] Test in multiplayer (dedicated server)
- [ ] Test with Optifine/Iris shaders
- [ ] Test with other player model mods
- [ ] Test with Wildfire's mod installed
- [ ] Edge case testing (empty alters, long names, special characters)

---

## Version 1.0.0 - Full Release

### Release Checklist
- [ ] All core features implemented
- [ ] All known bugs fixed
- [ ] Documentation complete
- [ ] Tested on multiple Minecraft versions
- [ ] Tested on all loaders (Forge, Fabric, NeoForge)
- [ ] Performance optimized
- [ ] Config file implemented
- [ ] README and wiki complete
- [ ] CurseForge and Modrinth pages set up
- [ ] Release trailer/screenshots ready

---

## Future Considerations (Post-1.0)

### Advanced Features (Backlog)
- [ ] Alter-specific inventories
- [ ] Alter-specific keybindings
- [ ] Front indicators (who's fronting visualization)
- [ ] Journal/log system for switches
- [ ] Statistics (switch frequency, time fronting)
- [ ] Integration with therapy tools (privacy-first!)
- [ ] VR support (for ViveCraft users)
- [ ] Proximity chat integration (show current alter to nearby players)

### Community Features (Backlog)
- [ ] Public API for other mods to integrate
- [ ] Datapack support for customization
- [ ] Resource pack support for custom UI
- [ ] Translation support (i18n)
- [ ] Discord Rich Presence (show current alter)

---

## Notes

**Development Philosophy:**
- Prioritize stability over features
- Privacy-first (all data stored locally)
- Mod compatibility is crucial
- Listen to community feedback
- Keep it simple and user-friendly

**Community Involvement:**
- Feature requests: GitHub Discussions
- Bug reports: GitHub Issues
- Testing: Discord community
- Contributions: Pull requests welcome!

---

Last Updated: 2024-12-01 (v0.1.7)
