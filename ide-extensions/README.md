# SCRUM Language IDE Extensions

This directory contains IDE extensions for the SCRUM programming language.

## Available Extensions

### VS Code Extension

Located in `vscode-scrum/`

**Features:**
- Syntax highlighting for `.scrum` files
- Code folding for Sprint/Story blocks
- Auto-closing brackets and quotes
- Comment toggling (Ctrl+/)
- Bracket matching

**Installation:**

**Method 1: Quick Install (Windows)**
```powershell
.\install-vscode.ps1
```
Or double-click `install-vscode.bat`

**Method 2: Manual Install**
```powershell
# Copy extension to VS Code extensions directory
xcopy /E /I vscode-scrum %USERPROFILE%\.vscode\extensions\scrum-language-0.1.0

# Restart VS Code
```

**Method 3: Development Mode**
```powershell
cd vscode-scrum
code .
# Press F5 to launch Extension Development Host
```

**Method 4: Package and Install**
```powershell
cd vscode-scrum
npm install -g @vscode/vsce
vsce package
code --install-extension scrum-language-0.1.0.vsix
```

### IntelliJ IDEA Plugin

Located in `intellij-scrum/`

**Features:**
- File type recognition for `.scrum` files
- Syntax highlighting
- File structure view
- Custom file icon

**Installation:**
1. Build the plugin JAR
2. IntelliJ IDEA → Settings → Plugins → ⚙️ → Install Plugin from Disk
3. Select the built JAR
4. Restart IntelliJ IDEA

See `intellij-scrum/README.md` for detailed instructions.

## Testing

See `TESTING.md` for comprehensive testing instructions for both extensions.

## File Structure

```
ide-extensions/
├── README.md                  # This file
├── TESTING.md                 # Testing guide
├── install-vscode.bat         # Windows batch installer
├── install-vscode.ps1         # PowerShell installer
├── vscode-scrum/              # VS Code extension
│   ├── package.json
│   ├── language-configuration.json
│   ├── README.md
│   └── syntaxes/
│       └── scrum.tmLanguage.json
└── intellij-scrum/            # IntelliJ IDEA plugin
    ├── plugin.xml
    ├── README.md
    └── src/
        └── scrum/intellij/
```

## Contributing

To modify the extensions:

1. **VS Code**: Edit `vscode-scrum/syntaxes/scrum.tmLanguage.json` for grammar changes
2. **IntelliJ**: Modify Java source files in `intellij-scrum/src/`
3. Test your changes
4. Commit to repository

## License

Same license as the SCRUM language project.
