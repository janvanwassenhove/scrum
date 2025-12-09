# GitHub Copilot Instructions

## Testing Requirements

When making changes to the Scrum language implementation, always add corresponding tests:

- **Language Features**: Any modifications to lexical parsing, statement parsing, expressions, operators, or language constructs must include test cases in the `test/scrum/` directory
- **New Functionality**: New language features require comprehensive test coverage demonstrating the feature works as intended
- **Bug Fixes**: Bug fixes should include regression tests that verify the fix and prevent future regressions
- **Test Location**: Follow the existing test structure - tests should mirror the `src/scrum/` package structure in `test/scrum/`

Test files should use JUnit and follow the existing naming convention (e.g., `LexicalParserTest.java`, `ScrumLanguageTest.java`).
