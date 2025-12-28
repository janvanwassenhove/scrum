#!/bin/bash
# SCRUM Language Release Script
# Automates the release process for new SDK versions

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
print_step() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}$1${NC}"
}

print_error() {
    echo -e "${RED}$1${NC}"
}

print_warning() {
    echo -e "${YELLOW}$1${NC}"
}

echo ""
echo "========================================"
echo "SCRUM Language Release Automation"
echo "========================================"
echo ""

# Get version from argument or user input
if [ -n "$1" ]; then
    NEW_VERSION="$1"
else
    while true; do
        read -p "Enter version number (e.g., 1.4.0, 1.4.0-beta.1): " NEW_VERSION
        if [ -z "$NEW_VERSION" ]; then
            print_error "Error: Version number cannot be empty"
            continue
        fi
        # Validate version format (supports pre-release)
        if echo "$NEW_VERSION" | grep -qE '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$'; then
            break
        else
            print_error "Error: Invalid version format. Use MAJOR.MINOR.PATCH[-PRERELEASE] (e.g., 1.4.0, 1.4.0-alpha, 1.4.0-beta.1)"
        fi
    done
fi

echo "Target Version: $NEW_VERSION"
echo ""



# Step 1: Prerequisites
print_step "Step 1: Checking Prerequisites..."

# Check Git
if ! command -v git &> /dev/null; then
    print_error "Error: Git not found"
    echo "Please install Git and try again"
    exit 1
fi
print_success "  [OK] Git installed"

# Check Maven
if ! command -v mvn &> /dev/null; then
    print_error "Error: Maven not found"
    echo "Please install Maven and try again"
    exit 1
fi
print_success "  [OK] Maven installed"

# Check Java
if ! command -v java &> /dev/null; then
    print_error "Error: Java not found"
    echo "Please install Java 21+ and try again"
    exit 1
fi
print_success "  [OK] Java installed"
echo ""

# Check for uncommitted changes
if ! git diff --quiet; then
    print_warning "Warning: You have uncommitted changes"
    read -p "Continue anyway? (y/N): " CONTINUE
    if [ "$CONTINUE" != "y" ] && [ "$CONTINUE" != "Y" ]; then
        echo "Release cancelled"
        exit 1
    fi
fi

# Check current branch
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$CURRENT_BRANCH" != "master" ]; then
    print_warning "Warning: Not on master branch (current: $CURRENT_BRANCH)"
    read -p "Continue anyway? (y/N): " CONTINUE
    if [ "$CONTINUE" != "y" ] && [ "$CONTINUE" != "Y" ]; then
        echo "Release cancelled"
        exit 1
    fi
fi
echo ""

# Step 2: Update version
print_step "Step 2: Updating Version in pom.xml..."

# Backup pom.xml
cp pom.xml pom.xml.bak

# Update version in pom.xml (target only the project version)
if sed -E "s|(<artifactId>scrum-language</artifactId>[[:space:]]*[[:cntrl:]]*[[:space:]]*<version>)[^<]+|\\1$NEW_VERSION|g" pom.xml > pom.xml.tmp && mv pom.xml.tmp pom.xml; then
    print_success "  [OK] Version updated to $NEW_VERSION"
    rm -f pom.xml.bak pom.xml.tmp
else
    print_error "Error: Failed to update pom.xml"
    mv pom.xml.bak pom.xml
    exit 1
fi
echo ""

# Step 3: Build
print_step "Step 3: Building SDK..."
echo ""

if ! mvn clean package; then
    echo ""
    print_error "Error: Build failed"
    echo "Please fix build errors and try again"
    exit 1
fi

echo ""
print_success "  [OK] Build successful"
echo ""

# Verify SDK zip
SDK_ZIP="target/scrum-language-$NEW_VERSION-sdk.zip"
if [ ! -f "$SDK_ZIP" ]; then
    print_error "Error: SDK zip not found"
    echo "Expected: $SDK_ZIP"
    exit 1
fi
print_success "  [OK] SDK zip created: $SDK_ZIP"
echo ""

# Step 4: Test installation (optional)
print_step "Step 4: Testing Installation (Optional)..."
read -p "Test installation locally? (y/N): " TEST_INSTALL

if [ "$TEST_INSTALL" = "y" ] || [ "$TEST_INSTALL" = "Y" ]; then
    echo ""
    echo "Installing to /tmp/scrum-test..."
    
    # Clean test directory
    rm -rf /tmp/scrum-test
    
    # Extract and install
    pushd target > /dev/null
    unzip -q "scrum-language-$NEW_VERSION-sdk.zip"
    pushd "scrum-$NEW_VERSION" > /dev/null
    ./installers/install.sh /tmp/scrum-test
    popd > /dev/null
    popd > /dev/null
    
    print_success "  [OK] Installation test successful"
    echo ""
fi

# Step 5: Commit
print_step "Step 5: Committing Changes..."

git add pom.xml
if ! git commit -m "Bump version to $NEW_VERSION"; then
    print_error "Error: Git commit failed"
    exit 1
fi

print_success "  [OK] Changes committed"
echo ""

# Step 6: Create tag
print_step "Step 6: Creating Git Tag..."

# Check if tag exists
if git tag -l | grep -q "v$NEW_VERSION"; then
    print_warning "Warning: Tag v$NEW_VERSION already exists"
    read -p "Delete existing tag? (y/N): " DELETE_TAG
    if [ "$DELETE_TAG" = "y" ] || [ "$DELETE_TAG" = "Y" ]; then
        git tag -d "v$NEW_VERSION"
        git push origin ":refs/tags/v$NEW_VERSION" 2>/dev/null || true
    else
        echo "Release cancelled"
        exit 1
    fi
fi

if ! git tag -a "v$NEW_VERSION" -m "Release version $NEW_VERSION"; then
    print_error "Error: Failed to create tag"
    exit 1
fi

print_success "  [OK] Tag v$NEW_VERSION created"
echo ""

# Step 7: Push
print_step "Step 7: Pushing to GitHub..."
echo ""

print_warning "This will push the commit and tag to GitHub, triggering the release workflow."
read -p "Push to GitHub? (y/N): " PUSH_CONFIRM

if [ "$PUSH_CONFIRM" != "y" ] && [ "$PUSH_CONFIRM" != "Y" ]; then
    echo ""
    echo "Release process stopped before pushing"
    echo ""
    echo "To complete manually:"
    echo "  git push origin master"
    echo "  git push origin v$NEW_VERSION"
    exit 0
fi

# Push commit
if ! git push origin master; then
    print_error "Error: Failed to push commit"
    exit 1
fi

# Push tag
if ! git push origin "v$NEW_VERSION"; then
    print_error "Error: Failed to push tag"
    exit 1
fi

# Success!
echo ""
print_success "========================================"
print_success "Release Process Complete!"
print_success "========================================"
echo ""
echo "Version: $NEW_VERSION"
echo "Tag: v$NEW_VERSION"
echo ""
echo "Next Steps:"
echo "1. Monitor GitHub Actions workflow:"
echo "   https://github.com/janvanwassenhove/scrum/actions"
echo ""
echo "2. Verify release when workflow completes:"
echo "   https://github.com/janvanwassenhove/scrum/releases"
echo ""
echo "3. Test installation from GitHub:"
echo "   Download and install SDK from release page"
echo ""

exit 0
