# Contributing to PluralCraft

Thank you for your interest in contributing to PluralCraft! 💜

## Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/MystiaTech/pluralcraft.git
   cd PluralCraft
   ```

2. **Requirements**
   - JDK 17 (Eclipse Adoptium recommended)
   - Git

3. **Build the mod**
   ```bash
   ./gradlew build
   ```

4. **Run in development**
   ```bash
   ./gradlew runClient  # Launch Minecraft client
   ./gradlew runServer  # Launch Minecraft server
   ```

## Version Bumping

We use semantic versioning (MAJOR.MINOR.PATCH).

**Windows:**
```batch
scripts\bump-version.bat [major|minor|patch]
```

**Linux/Mac:**
```bash
./scripts/bump-version.sh [major|minor|patch]
```

## Making Changes

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes**
   - Write clean, documented code
   - Follow existing code style
   - Test your changes!

3. **Update CHANGELOG.md**
   - Add your changes under `[Unreleased]`
   - Use format: `### Added/Changed/Fixed`

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "Description of changes"
   ```

5. **Push and create a Pull Request**
   ```bash
   git push origin feature/your-feature-name
   ```

## Code Style

- Use clear, descriptive variable names
- Comment complex logic
- Keep functions focused and small
- Add JavaDoc comments for public methods
- Test on both client and server sides when applicable

## Pull Request Guidelines

- Describe what your PR does
- Reference any related issues
- Include screenshots/videos for UI changes
- Make sure the build passes
- Be respectful and patient!

## Release Process

1. Update CHANGELOG.md with all changes
2. Bump version: `./scripts/bump-version.bat minor`
3. Commit: `git commit -am "Release v0.2.0"`
4. Tag: `git tag -a v0.2.0 -m "Release v0.2.0"`
5. Push: `git push && git push --tags`
6. GitHub Actions will automatically build and create the release!

## Questions?

Feel free to open an issue or start a discussion! We're here to help! 💜

---

Made with love for the plural community 🌈
