# Changelog
All notable changes to PluralCraft will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Planned
- Chat formatting with system/alter names
- PronounDB integration
- Cosmetic Armor integration
- Client-side 3D skin rendering (actual visual application of custom skins)
- Wildfire's Gender Mod GUI integration (open their screen from our button)
- Fabric port
- NeoForge port

## [0.2.0] - 2024-12-02
### Added
- **🔥 BODY RENDERING SYSTEM!** - Per-alter body customization is now VISUALLY RENDERED!
  - PlayerBodyRenderer handles real-time model modification
  - Breast size actually shows on your character
  - Hip width modifies leg positioning
  - Body curves affect overall body shape
  - Male bulge modifies leg geometry
  - **OVERRIDES other mods (MCA, Wildfire) when custom body is enabled!**
  - When custom body disabled, resets to vanilla (removes other mod effects)

### Technical
- Created PlayerBodyRenderer with RenderPlayerEvent.Pre hook
- Modifies PlayerModel geometry in real-time based on current alter
- Scale and position modifications for body parts (body, arms, legs)
- Full override capability to fix conflicts with MCA Reborn and Wildfire
- Works seamlessly with alter switching (Quick Switch keybind)

### Notes
- This is a MAJOR feature release! Body customization is now fully functional!
- Per-alter bodies mean different alters can have different body types
- System can have feminine character, but alter can override to masculine (and vice versa)
- Fixes the issue where MCA/Wildfire settings couldn't be overridden per-alter

## [0.1.9] - 2024-12-02
### Fixed
- **CRITICAL: Wildfire Mod Conflict** - Namespaced NBT tags to prevent conflicts
  - Body customization data now uses `pluralcraft_` prefix
  - Prevents Wildfire's Gender Mod from reading our data
  - Fixes issue where disabling custom body didn't remove visual changes
  - Includes backwards compatibility for old saves
  - No more double breasts when using both mods!

### Technical
- NBT tags now properly namespaced: `pluralcraft_breastSize`, etc.
- Automatic migration from old tag names to new namespaced tags
- GitHub Actions workflow now has proper permissions for releases

## [0.1.8] - 2024-12-02
### Fixed
- **Body Customization Reset** - Disabling custom body now properly resets model to defaults
  - Values reset to 0.3/0.5/0/0.5 when unchecking "Enable Custom Body"
  - Applies both when clicking Done and when closing screen
- **UI Layout Improvements** - Fixed overlapping elements in Body Customization screen
  - Increased spacing between sliders (30→35px)
  - Increased padding after checkbox (10→15px)
  - Repositioned info text to avoid overlap
- **System ID Card Placement** - Item no longer places on blocks
  - Added useOn() override to prevent accidental placement
  - Now only works when right-clicking in air
  - No more accidentally using it on logs/trees!

## [0.1.7] - 2024-12-01
### Added
- **Skin Customization System!** - Each alter can now have their own custom skin
  - SkinCustomizationScreen GUI with URL input and file upload support
  - Skin URL storage per alter in AlterProfile
  - Automatic skin application when switching alters (hooks into client tick)
  - Skin preview in customization screen
  - "Customize Skin" button in both EditAlterScreen and AddAlterScreen
  - SkinManager handles loading, caching, and applying skins from URLs or files
  - **WORKING file upload with JFileChooser** - Select .png files directly!
  - Reset to default skin functionality

- **Edit Details Screen!** - FULLY FUNCTIONAL alter editing
  - Edit name, age, pronouns, and bio for existing alters
  - Proper validation and error messages
  - Saves changes immediately

- **Communication Method Screen!** - Select how alters communicate
  - Choose from 5 communication methods (Verbal, Nonverbal, Text Only, Selective Mute, Mixed)
  - Shows descriptions for each method
  - Visual selection with arrow indicators

- **System ID Card GUI!** - Beautiful visual ID card
  - Card-style design with colored borders
  - Shows system name and current alter info
  - Displays player model like a photo
  - Lists all system members
  - Opens with right-click instead of chat spam

- **Quick Switch Keybind!** - FULLY FUNCTIONAL alter switching
  - Press the Quick Switch key (default: K) to cycle through alters
  - Automatically applies skin when switching
  - Shows message with new alter name
  - Works from anywhere in the game

- **System Name Editor!** - Change your system name anytime
  - Click "System: [name]" button in Alter Management screen
  - Edit system name with validation
  - Saves immediately to disk

### Changed
- KeyInputHandler now includes skin switching logic
- Skin settings persist across sessions via NBT
- System ID Card now opens a GUI instead of sending chat messages
- Wildfire integration attempts to detect keybind and provides better guidance

### Fixed
- BodyCustomizationScreen now properly saves data when closing (was missing saveData() call)
- Wildfire keybind message now accurate (checks user's keybinds instead of wrong key)
- Edit Details button now functional instead of placeholder
- Communication Method button now functional instead of placeholder

### Technical
- Created `client.skin.SkinManager` for skin handling
- Uses NativeImage and DynamicTexture for texture loading
- Caches skin files in `pluralcraft/skins/` directory
- Async skin downloading to prevent blocking

### Known Issues
- Skin visual rendering not yet implemented (textures load but don't display on player model)
- Similar to body customization, actual 3D rendering deferred to future version

## [0.1.6] - 2024-12-01
### Added
- **PROPER DRAG SLIDERS!** - Real AbstractSliderButton widgets with smooth dragging
- **1% Precision** - Fine-tune body settings from 0-100% with 1% increments (not 10%!)
- **Player Model Preview** - See your player model rotating in real-time while customizing
- **Wildfire Mod Coexistence** - Both mods can now be used together!
  - Shows "PluralCraft Body Settings" button when Wildfire is present
  - Shows "Wildfire's Gender Mod (Default)" button to use theirs
  - User can choose which mod's customization to use
- Custom BodySlider widget class for reusable slider components

### Changed
- Removed restriction against using body customization with Wildfire
- Updated button text to clarify which mod's settings you're using
- Body customization screen now has left/right layout (sliders left, preview right)
- Model preview follows mouse movement

### Fixed
- Sliders now respond to dragging instead of clicking through values
- Body customization values properly update in real-time
- Player model rendering uses correct 1.20.1 API

### Technical
- BodySlider extends AbstractSliderButton for proper drag handling
- ValueConsumer functional interface for slider callbacks
- Model preview uses InventoryScreen.renderEntityInInventoryFollowsMouse

## [0.1.5] - 2024-12-01
### Added
- **Edit Existing Alters!** - Click on any alter in the list to edit them
- EditAlterScreen - New GUI for modifying existing alters
- Body customization now available for existing alters (not just new ones)
- **Wildfire's Gender Mod Compatibility!**
  - Automatically detects if Wildfire's mod is installed
  - Disables our body customization if Wildfire is present
  - Shows helpful message: "Use Wildfire's Mod instead!"
  - Prevents double-rendering of body features

### Fixed
- Alter list now shows "click to edit" label
- Each alter is now a clickable button in the management screen
- Body customization properly saves when returning from screen

### Technical
- WildfireCompat helper class for mod detection
- EditAlterScreen with access to body customization
- Improved alter list UI with buttons

### Known Issues
- Body sliders are click-through buttons (not drag sliders yet)
- No visual preview of body changes (3D rendering not implemented)
- Camera/viewpoint doesn't help see changes yet

## [0.1.4] - 2024-12-01
### Added
- **Body Customization System** - Each alter can have unique body appearance!
  - Breast size slider (0-100%)
  - Hip width slider (0-100%)
  - Male bulge slider (0-100%) - Feature Wildfire didn't add!
  - Body curves slider (0-100%) - Overall curviness control
  - Enable/disable custom body per alter
  - "Customize Body" button in Add Alter screen
- BodyCustomization data class with NBT persistence
- BodyCustomizationScreen GUI with interactive sliders
- Body settings saved per alter and persist across sessions

### Technical
- BodyCustomization integrated with AlterProfile
- All body settings stored in NBT format
- Temporary alter for body customization during creation
- Reset to defaults functionality

### Notes
- Body customization GUI is complete and functional
- Actual 3D model rendering will be added in future update
- Settings are saved and ready for when rendering is implemented

## [0.1.3] - 2024-12-01
### Added
- **Communication Method Support** - Alters can now have different communication styles!
  - Verbal (default)
  - Nonverbal (Emojis) - Perfect for mute/nonverbal alters
  - Text Only
  - Selectively Mute
  - Mixed
- **Emoji Picker GUI** - Press E to open!
  - Categories: Emotions, Gestures, People, Hearts, Symbols, Common
  - Click any emoji to send it to chat
  - Scroll support for browsing all emojis
  - Perfect for nonverbal communication
- Communication method field in AlterProfile (saved to NBT)
- Keybinding for emoji picker (default: E key)

### Technical
- CommunicationMethod enum with 5 types
- EmojiCategory system with 100+ emojis organized by category
- EmojiPickerScreen with grid layout and scrolling

## [0.1.2] - 2024-12-01
### Added
- **System ID Card item** - Right-click to view system info!
  - Shows system name
  - Displays currently fronting alter with full details
  - Lists all alters with pronouns
  - Beautiful formatted output in chat
- Add Alter GUI screen with form fields
  - Name, age, pronouns, bio fields
  - Input validation
  - Returns to main screen after creation
- Commands now work WITHOUT cheats enabled!
  - Available to all players in singleplayer and multiplayer
  - No OP permission required

### Fixed
- Fixed GitHub repository links in mods.toml
- Updated author credits to "MystiaTech & Claude (Anthropic)"
- Commands `.requires()` now returns true for everyone

### Technical
- SystemIDItem with formatted chat display
- AddAlterScreen with EditBox components
- Command registration without OP requirement

## [0.1.1] - 2024-12-01
### Added
- **Mod integration system** - Save and load other mods' settings per alter!
  - Framework for mod integrations
  - IModIntegration interface for extensibility
  - ModIntegration manager with auto-detection
  - Documentation for mod developers (docs/MOD_INTEGRATION.md)
  - Support for Wildfire's Gender Mod (framework ready)
  - Support for Cosmetic Armor (framework ready)
- Keybinding system (default: P key)
- **Alter Management GUI** - Full-featured screen!
  - System name display
  - Current alter display
  - Add new alter button
  - Switch alter button (cycles through alters)
  - List of all alters with current indicator
- Commands for alter management
  - `/pluralcraft system setname <name>`
  - `/pluralcraft alter add <name> <age> <pronouns>`
  - `/pluralcraft alter switch <name>`
  - `/pluralcraft alter list`

### Technical
- KeyBindings registration system
- KeyInputHandler for detecting key presses
- AlterManagementScreen with button components
- Language file (en_us.json) for translations
- Mod settings storage per alter (CompoundTag in AlterProfile)

## [0.1.0] - 2024-12-01
### Added
- Initial project setup
- Core data structures (AlterProfile, SystemProfile)
- Data persistence system (saves to world folder)
- Client/server side detection
- Mods Optimizer compatibility
- Basic mod infrastructure

### Technical
- Forge 1.20.1 support (47.3.0)
- Auto-versioning system
- Build automation
