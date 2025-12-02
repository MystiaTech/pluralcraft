# PluralCraft 💫

**A Minecraft mod for plural systems and DID/OSDD communities to express themselves authentically in-game!**

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net/)
[![Forge](https://img.shields.io/badge/Forge-47.3.0-orange.svg)](https://files.minecraftforge.net/)
[![License](https://img.shields.io/badge/License-LGPL--3.0-blue.svg)](LICENSE)

## 🌟 Features

### System & Alter Management
- Create your system with a custom name
- Add multiple alters with unique profiles (name, age, pronouns, bio)
- Communication methods (Verbal, Nonverbal/Emojis, Text Only, Selectively Mute, Mixed)
- Custom body appearance per alter (breast size, hip width, male bulge, body curves)
- Switch between alters without relogging
- Edit existing alters by clicking them in the list
- System ID Card item - Right-click to view system info

### Body Customization
- Proper drag sliders with 1% precision (0-100%)
- Player model preview - See your character while customizing
- Four customization options: Breast size, Hip width, Male bulge, Body curves
- **Wildfire's Gender Mod compatible!** Use both mods together
- Settings save per alter and persist across sessions

### Skin Customization (NEW in v0.1.7!)
- **Per-Alter Custom Skins!** - Each alter can have their own unique skin
- Skin URL support (load from NameMC, Minecraft Heads, etc.)
- **File upload support!** - Choose .png files directly from your computer
- Reset to default skin functionality
- Player model preview in customization screen
- Skins automatically apply when switching alters
- Settings persist across sessions

### Emoji Communication
- Emoji Picker GUI (Press E) - Perfect for nonverbal/mute alters!
- 100+ emojis in 6 categories
- Click any emoji to send to chat

### Keybindings
- **P** - Open Alter Management
- **E** - Open Emoji Picker
- **K** - Quick Switch (cycle through alters)

## 📥 Installation

1. Download latest `.jar` from [Releases](https://github.com/MystiaTech/pluralcraft/releases)
2. Place in `.minecraft/mods` folder
3. Launch Minecraft with Forge 1.20.1
4. Press **P** in-game!

## 🎮 Quick Start

1. Press **P** → "Add New Alter"
2. Enter name, age, pronouns
3. (Optional) Click "Customize Body" to set body appearance
4. (Optional) Click "Customize Skin" to set a unique skin URL
5. Done!

## 📝 Commands

All commands work **without cheats**!

```
/pluralcraft system setname <name>
/pluralcraft alter add <name> <age> <pronouns>
/pluralcraft alter switch <name>
/pluralcraft alter list
```

## 🔧 For Developers

See [docs/MOD_INTEGRATION.md](docs/MOD_INTEGRATION.md) for mod integration API!

### Building
```bash
./gradlew build  # Output in build/libs/
```

## 🤝 Compatibility

- **Wildfire's Gender Mod**: Fully compatible! Use both together
- **Mods Optimizer**: Full support

## 🐛 Known Issues

- Body settings save but don't render visually yet (3D rendering coming soon)
- Skin settings save but don't render visually yet (3D skin rendering coming soon)
- Wildfire GUI button provides keybind info instead of direct opening (API integration in progress)

See [CHANGELOG.md](CHANGELOG.md) for full version history.

## 📜 License

LGPL-3.0 - See [LICENSE](LICENSE)
Distribution restricted to CurseForge and Modrinth

## 💖 Credits

- MystiaTech & Claude (Anthropic)
- Inspired by the plural/DID/OSDD community

## 📧 Support

- [GitHub Issues](https://github.com/MystiaTech/pluralcraft/issues)
- [GitHub Discussions](https://github.com/MystiaTech/pluralcraft/discussions)

---

**Enjoy expressing your authentic selves in Minecraft! 💫**
