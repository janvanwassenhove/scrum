# SCRUM Language Support for VS Code

This extension provides syntax highlighting and language support for SCRUM programming language files (`.scrum`).

## Features

- **Syntax Highlighting** for SCRUM keywords, directives, and constructs
- **Code Comments** support (line comments with `--` and block comments with `--[[ ]]`)
- **Bracket Matching** for `{}`, `[]`, and `()`
- **Auto-closing Pairs** for brackets and quotes
- **Code Folding** for STORY, API, and control structure blocks

## Supported SCRUM Syntax

- Directives: `#SPRINTGOAL`, `#REVIEW`, `#IMPEDIMENT`, `#RETROSPECTIVE`, `#BACKLOG`
- API Definitions: `I WANT TO DEFINE API`, `I WANT TO DEFINE ENDPOINT`
- User Stories: `USER STORY`, `END OF STORY`, `USING`, `RETURN ANSWER`
- Control Structures: `IF`, `ELSEIF`, `ELSE`, `WHILE`, `FOR`
- Keywords: `IS`, `ARE`, `SAY`, `ASK`, `CALL`, `WITH`

## Installation

### From Source

1. Copy this folder to your VS Code extensions directory:
   - Windows: `%USERPROFILE%\.vscode\extensions\`
   - macOS/Linux: `~/.vscode/extensions/`

2. Restart VS Code

### Testing in Development

1. Open this folder in VS Code
2. Press `F5` to launch Extension Development Host
3. Open any `.scrum` file to test

## Usage

Simply open any file with `.scrum` extension and syntax highlighting will be applied automatically.

## Release Notes

### 0.1.0

Initial release with basic syntax highlighting support.
