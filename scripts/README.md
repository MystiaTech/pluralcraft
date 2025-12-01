# Build Scripts

Helper scripts for PluralCraft development.

## bump-version

Automatically bumps the mod version in `gradle.properties`.

**Windows:**
```batch
scripts\bump-version.bat [major|minor|patch]
```

**Linux/Mac:**
```bash
./scripts/bump-version.sh [major|minor|patch]
```

**Examples:**
- `bump-version.bat patch` - 0.1.0 → 0.1.1 (bug fixes)
- `bump-version.bat minor` - 0.1.0 → 0.2.0 (new features)
- `bump-version.bat major` - 0.1.0 → 1.0.0 (breaking changes)

The script will tell you what to do next (update changelog, commit, tag, push).
