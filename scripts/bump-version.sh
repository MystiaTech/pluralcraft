#!/bin/bash
# Script to bump the mod version
# Usage: ./scripts/bump-version.sh [major|minor|patch]

set -e

GRADLE_PROPERTIES="gradle.properties"
TYPE=${1:-patch}

# Read current version
CURRENT_VERSION=$(grep "mod_version=" $GRADLE_PROPERTIES | cut -d'=' -f2)

# Parse version parts
IFS='.' read -ra VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR="${VERSION_PARTS[0]}"
MINOR="${VERSION_PARTS[1]}"
PATCH="${VERSION_PARTS[2]}"

# Bump version based on type
case $TYPE in
  major)
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    ;;
  minor)
    MINOR=$((MINOR + 1))
    PATCH=0
    ;;
  patch)
    PATCH=$((PATCH + 1))
    ;;
  *)
    echo "Invalid version type. Use: major, minor, or patch"
    exit 1
    ;;
esac

NEW_VERSION="${MAJOR}.${MINOR}.${PATCH}"

echo "Bumping version from $CURRENT_VERSION to $NEW_VERSION"

# Update gradle.properties
sed -i "s/mod_version=.*/mod_version=$NEW_VERSION/" $GRADLE_PROPERTIES

echo "Version bumped to $NEW_VERSION!"
echo ""
echo "Next steps:"
echo "1. Update CHANGELOG.md with changes"
echo "2. Commit: git commit -am 'Bump version to $NEW_VERSION'"
echo "3. Tag: git tag -a v$NEW_VERSION -m 'Release v$NEW_VERSION'"
echo "4. Push: git push && git push --tags"
