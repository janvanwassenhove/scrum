# SCRUM Language Plugin for IntelliJ IDEA

This plugin provides language support for SCRUM programming language files (`.scrum`) in IntelliJ IDEA.

## Features

- **Syntax Highlighting**: Keywords, strings, numbers, comments, and operators are highlighted
- **File Type Recognition**: `.scrum` files are recognized as SCRUM language files
- **Code Structure**: View the structure of your SCRUM files
- **Comment Toggle**: Use Ctrl+/ to toggle line comments
- **Bracket Matching**: Automatic bracket and parenthesis matching
- **Custom File Icon**: SCRUM files display with a custom icon in the project view

## Installation

### Method 1: Build and Install from Source

1. **Prerequisites**: 
   - IntelliJ IDEA (Community or Ultimate)
   - JDK 11 or higher
   - IntelliJ Plugin DevKit

2. **Setup Plugin Development**:
   ```bash
   cd ide-extensions/intellij-scrum
   ```

3. **Open in IntelliJ IDEA**:
   - File → Open → Select `intellij-scrum` directory
   - Configure SDK if prompted

4. **Build the Plugin**:
   - Build → Build Project
   - Or use Gradle if configured: `gradle buildPlugin`

5. **Run in Sandbox**:
   - Run → Run 'Plugin'
   - This launches a new IntelliJ instance with the plugin loaded

6. **Install Permanently**:
   - Build the plugin JAR
   - Settings → Plugins → ⚙️ → Install Plugin from Disk
   - Select the built JAR file
   - Restart IntelliJ IDEA

### Method 2: Manual Compilation

```bash
cd ide-extensions/intellij-scrum

# Compile Java files
javac -cp "path/to/intellij/lib/*" -d out/production/intellij-scrum src/scrum/intellij/*.java

# Create JAR with plugin.xml
cd out/production
jar cvf scrum-intellij-plugin.jar -C intellij-scrum . -C ../../resources .

# Install via Settings → Plugins → Install from Disk
```

## Project Structure

```
intellij-scrum/
├── src/
│   └── scrum/
│       └── intellij/
│           ├── ScrumLanguage.java              # Language definition
│           ├── ScrumFileType.java              # File type definition
│           ├── ScrumParserDefinition.java      # Parser setup
│           ├── ScrumLexer.java                 # Lexical analyzer
│           ├── ScrumParser.java                # Parser
│           ├── ScrumTokenTypes.java            # Token definitions
│           ├── ScrumSyntaxHighlighter.java     # Syntax highlighting
│           ├── ScrumSyntaxHighlighterFactory.java
│           ├── ScrumCommenter.java             # Comment support
│           ├── ScrumBraceMatcher.java          # Bracket matching
│           ├── ScrumPsiElement.java            # PSI element
│           └── ScrumFile.java                  # PSI file
├── resources/
│   ├── META-INF/
│   │   └── plugin.xml                          # Plugin configuration
│   └── icons/
│       └── scrum-icon.png                      # File icon (optional)
└── README.md                                   # This file
```

## SCRUM Language Keywords

The plugin recognizes the following SCRUM language constructs:

### Control Keywords
- `Sprint`, `EndSprint`
- `Story`, `Task`, `Deliver`
- `IF`, `THEN`, `ELSE`, `ENDIF`
- `WHILE`, `ENDWHILE`
- `FOR`, `ENDFOR`

### API Keywords
- `I WANT TO DEFINE API`, `END OF API`
- `I WANT TO DEFINE ENDPOINT`, `END OF ENDPOINT`
- `BASE IS`, `METHOD IS`, `PATH IS`
- `QUERY_PARAMS ARE`, `RETURNS IS`

### Directives
- `#SPRINTGOAL`
- `#REVIEW`
- `#IMPEDIMENT`
- `#RETROSPECTIVE`
- `#BACKLOG`

### Comments
- Line comments: `-- comment`
- Block comments: `--[[ comment ]]`

## Testing the Plugin

1. **Open a SCRUM file** in the sandbox IDE
2. **Verify syntax highlighting** for keywords, strings, numbers
3. **Test comment toggle** (Ctrl+/)
4. **Check bracket matching** by clicking on brackets
5. **View file structure** (Alt+7 or View → Tool Windows → Structure)

## Development

To modify the plugin:

1. **Syntax Highlighting**: Edit `ScrumSyntaxHighlighter.java` and `ScrumLexer.java`
2. **Add Keywords**: Update `ScrumLexer.java` → `getKeywordType()` method
3. **Change Colors**: Modify `ScrumSyntaxHighlighter.java` text attributes
4. **Parser Logic**: Edit `ScrumParser.java` for AST construction

## Compatibility

- **IntelliJ IDEA**: 2020.3+ (Build 203+)
- **Java**: 11+
- **Platform**: Windows, macOS, Linux

## Troubleshooting

### Plugin Not Loading
- Check `plugin.xml` is in `resources/META-INF/`
- Verify all Java classes compiled without errors
- Check IntelliJ Event Log for error messages

### Syntax Highlighting Not Working
- Verify `ScrumSyntaxHighlighterFactory` is registered in `plugin.xml`
- Check lexer returns correct token types
- Ensure `ScrumLanguage.INSTANCE` is initialized

### File Type Not Recognized
- Verify `ScrumFileType` registration in `plugin.xml`
- Check file extension is set to "scrum"
- Restart IntelliJ after installation

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Make your changes
3. Test thoroughly in IntelliJ sandbox
4. Submit a pull request

## License

Same license as the SCRUM language project.

## Support

For issues or questions:
- GitHub Issues: https://github.com/janvanwassenhove/scrum/issues
- Documentation: See main SCRUM repository README
