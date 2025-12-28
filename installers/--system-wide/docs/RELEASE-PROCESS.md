# SCRUM Language Release Process

This guide describes how to release a new version of the SCRUM Programming Language SDK.

## Prerequisites

- Git installed and configured
- Maven 3.6+ installed
- Java 21+ installed
- Write access to the GitHub repository
- GitHub Personal Access Token (for automated releases)

## Release Overview

The SCRUM release process involves:
1. Version bumping in `pom.xml`
2. Building and testing the SDK
3. Committing version changes
4. Creating and pushing a Git tag
5. GitHub Actions automatically builds and publishes the release

## Manual Release Process

### Step 1: Update Version Number

Edit `pom.xml` and update the version:

```xml
<groupId>scrum</groupId>
<artifactId>scrum-language</artifactId>
<version>1.4.0</version>  <!-- Update this -->
```

### Step 2: Build and Test

Build the SDK and run all tests:

```bash
mvn clean package
```

Verify:
- All tests pass (50/50)
- SDK zip is created: `target/scrum-language-{version}-sdk.zip`
- JAR is created: `target/scrum-{version}.jar`

### Step 3: Test Installation Locally

Extract and test the SDK:

```powershell
# PowerShell
cd target
Expand-Archive -Path scrum-language-1.4.0-sdk.zip -DestinationPath .
cd scrum-1.4.0
.\installers\install.ps1 -InstallDir C:\scrum-test

# Test
scrum --version
scrum C:\scrum-test\examples\HelloWorld.scrum
```

### Step 4: Commit Version Changes

```bash
git add pom.xml
git commit -m "Bump version to 1.4.0"
```

### Step 5: Create and Push Git Tag

```bash
# Create annotated tag
git tag -a v1.4.0 -m "Release version 1.4.0"

# Push commit and tag
git push origin master
git push origin v1.4.0
```

### Step 6: GitHub Actions Automatically Builds Release

Once the tag is pushed, GitHub Actions will:
1. Build the SDK
2. Run all tests
3. Create a GitHub Release
4. Upload the SDK zip file as a release asset

Monitor the workflow at: `https://github.com/janvanwassenhove/scrum/actions`

## Automated Release Process

Use the provided `release.bat`, `release.ps1`, or `release.sh` script to automate the entire process:

### Windows (Batch)

```cmd
# Interactive mode (recommended)
release.bat

# Or specify version as argument
release.bat 1.4.0
```

### Windows (PowerShell)

```powershell
# Interactive mode (recommended)
.\release.ps1

# Or specify version as parameter
.\release.ps1 -Version 1.4.0

# With options to skip tests or installation testing
.\release.ps1 -SkipTests -SkipInstallTest
```

### Linux/macOS

```bash
# Interactive mode (recommended)
./release.sh

# Or specify version as argument
./release.sh 1.4.0
```

When run in interactive mode, the script will prompt you:
```
========================================
SCRUM Language Release Automation
========================================

Enter version number (e.g., 1.4.0): 1.4.0
Target Version: 1.4.0
```

The script will:
1. Prompt for version number (if not provided as argument)
2. Validate prerequisites (Git, Maven, Java)
3. Validate version format (MAJOR.MINOR.PATCH)
4. Update version in `pom.xml`
5. Build and test the SDK
6. Test installation locally (optional)
7. Commit changes
8. Create and push Git tag
9. Trigger GitHub Actions release workflow

## Release Checklist

Before releasing, ensure:

- [ ] All tests pass (`mvn clean test`)
- [ ] Version number follows semantic versioning (MAJOR.MINOR.PATCH)
- [ ] `LANGUAGE-REFERENCE.md` is up to date with any language changes
- [ ] `API-DEFINITIONS.md` is up to date with any API changes
- [ ] `README.md` reflects current version and features
- [ ] Examples in `development/examples/` all run successfully
- [ ] IDE extensions are compatible with new version
- [ ] No uncommitted changes in working directory

## Post-Release Tasks

After GitHub Actions completes:

1. **Verify GitHub Release**
   - Go to: `https://github.com/janvanwassenhove/scrum/releases`
   - Verify release `v1.4.0` is published
   - Verify SDK zip is attached as asset

2. **Update Documentation**
   - Update installation instructions if needed
   - Update SDK-INSTALLATION.md with new version references

3. **Announce Release**
   - Update project README with latest version badge
   - Notify users of new release

4. **Test Installation from GitHub**
   ```powershell
   # Download from GitHub
   Invoke-WebRequest -Uri "https://github.com/janvanwassenhove/scrum/releases/download/v1.4.0/scrum-language-1.4.0-sdk.zip" -OutFile scrum-sdk.zip
   
   # Install
   Expand-Archive -Path scrum-sdk.zip -DestinationPath .
   cd scrum-1.4.0
   .\installers\install.ps1
   
   # Verify
   scrum --version
   ```

## Version Numbering Guidelines

Follow [Semantic Versioning 2.0.0](https://semver.org/):

- **MAJOR** (1.x.x): Breaking changes to language syntax or API
  - Example: Removing or changing existing keywords
  - Example: Incompatible changes to API definitions

- **MINOR** (x.1.x): New features, backward compatible
  - Example: New language constructs (intents, story elements)
  - Example: New API capabilities
  - Example: Performance improvements

- **PATCH** (x.x.1): Bug fixes, backward compatible
  - Example: Fixing parser errors
  - Example: Correcting runtime exceptions
  - Example: Documentation updates

### Pre-release Versions

For alpha, beta, and release candidate versions, append pre-release identifiers:

- **Alpha** (1.4.0-alpha, 1.4.0-alpha.1): Early development, unstable
- **Beta** (1.4.0-beta, 1.4.0-beta.2): Feature complete, testing phase
- **Release Candidate** (1.4.0-rc.1): Final testing before release

**Examples**:
```bash
# Alpha releases
release.bat 1.4.0-alpha
release.bat 1.4.0-alpha.1
release.bat 1.4.0-alpha.2

# Beta releases  
release.bat 1.4.0-beta
release.bat 1.4.0-beta.1

# Release candidates
release.bat 1.4.0-rc.1
release.bat 1.4.0-rc.2

# Final release
release.bat 1.4.0
```

**Pre-release Guidelines**:
- Use `-alpha` for early unstable versions
- Use `-beta` for feature-complete testing versions
- Use `-rc` (release candidate) for final testing
- Number increments: `-alpha.1`, `-alpha.2`, `-beta.1`, `-rc.1`
- GitHub will automatically mark pre-release versions appropriately

## Rollback Procedure

If a release has critical issues:

1. **Delete the Git tag**
   ```bash
   git tag -d v1.4.0
   git push origin :refs/tags/v1.4.0
   ```

2. **Delete the GitHub Release**
   - Go to GitHub Releases page
   - Edit the release and delete it

3. **Revert version changes**
   ```bash
   git revert HEAD
   git push origin master
   ```

4. **Fix issues and release new patch version**
   - Fix the critical issues
   - Release as v1.4.1

## Troubleshooting

### Build Fails

```bash
# Clean and rebuild
mvn clean package -X

# Check Java version
java -version  # Must be 21+

# Check Maven version
mvn -version  # Must be 3.6+
```

### GitHub Actions Workflow Fails

1. Check workflow logs at GitHub Actions page
2. Verify GitHub token permissions
3. Ensure all required files are committed
4. Re-run failed workflow from GitHub UI

### Tag Already Exists

```bash
# Delete local tag
git tag -d v1.4.0

# Delete remote tag
git push origin :refs/tags/v1.4.0

# Recreate tag
git tag -a v1.4.0 -m "Release version 1.4.0"
git push origin v1.4.0
```

## GitHub Actions Workflow

The release workflow (`.github/workflows/release.yml`) is triggered when a tag starting with `v` is pushed:

```yaml
on:
  push:
    tags:
      - 'v*'
```

The workflow:
1. Checks out code
2. Sets up Java 21
3. Builds with Maven
4. Runs tests
5. Creates GitHub Release
6. Uploads SDK zip as release asset

## Contact

For questions about the release process:
- Open an issue on GitHub
- Contact: Jan Van Wassenhove
- Repository: https://github.com/janvanwassenhove/scrum
