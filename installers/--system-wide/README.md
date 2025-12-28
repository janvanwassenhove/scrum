![SCRUM language](asset/banner.png)

# SCRUM
The home of the SCRUM programming language.
Designed for being a language which is understandable for business as well as for developers 
and losely inspired by the Scrum (software development) framework (hence the name of course).

## Table of Contents

- [Meet Scrummy! 🎉](#meet-scrummy-)
- [Why?](#why)
- [Revolutionary #INTENT: Natural Language Programming 🚀](#revolutionary-intent-natural-language-programming-)
- [Hello World](#hello-world)
- [Running A Scrum Program](#running-a-scrum-program)
- [IDE Support](#ide-support)
- [Language Reference](#language-reference)
- [API Definitions](#api-definitions)
- [Error Handling](#error-handling)
- [Documentation](#documentation)
- [Credits](#credits)

## Meet Scrummy! 🎉

<p align="center">
  <img src="asset/scrummy_transparant.png" alt="Scrummy - The SCRUM Mascot" width="300"/>
</p>

**Scrummy** is the official mascot of the SCRUM programming language! This friendly companion represents the approachable and collaborative spirit of SCRUM. With those big eyes and a cheerful smile, Scrummy is here to guide you through your journey of becoming a true SCRUM MASTER PROGRAMMER!


## Why?
So you can become an actual Master of SCRUM programming instead of being just a 'Scrum Master' (without even coding)!
Or in other words a SCRUM MASTER PROGRAMMER!

Inspired by being a Rockstar developer (the programming language developed by Dylan Beattie) 
and wanting to really comprehend the creation and work of an actual programming language I decided to develop my own.
As by origin being a Java Developer and because everyone has Java on his computer I used Java as base for the new language.

## Revolutionary #INTENT: Natural Language Programming 🚀

**SCRUM introduces a groundbreaking approach to programming:** the `#INTENT` construct that represents **a new level in programming languages**. Write natural language descriptions that are **automatically transformed into executable code** at compile time using Large Language Models (LLMs).

### Traditional Programming vs. SCRUM #INTENT

**Traditional approach:**
```java
// You write explicit implementation
Scanner scanner = new Scanner(System.in);
System.out.print("Enter your age: ");
int age = scanner.nextInt();
int birthYear = 2024 - age;
System.out.println("You were born in: " + birthYear);
```

**SCRUM #INTENT approach:**
```scrum
USER STORY "ComputeBirthYear"
    #INTENT
    I want to ask the user for their current age.
    The input will be used to calculate and display their birth year 
    based on the current year 2024.
    #END INTENT
END
```

**The `#INTENT` block automatically becomes fully functional SCRUM code at compile time!**

### How It Works

1. **Write Intent**: Describe what you want in natural language
2. **Compile-time AI**: LLM transforms your intent into valid SCRUM syntax
3. **Auto-validation**: Generated code is verified before execution
4. **Seamless Execution**: Your program runs as if you wrote the code manually

### Supported AI Providers

- **OpenAI** (GPT-4o, GPT-4o-mini, o1, o3-mini)
- **Anthropic Claude** (Sonnet 4, Sonnet 4.5) 
- **Groq** (Ultra-fast, free tier available)
- **Cerebras** (High performance)
- **Together.ai** (Multiple open-source models)
- **Ollama** (Local, completely free)

**Quick start with Groq (free):**
```bash
export SCRUM_API_KEY="your-groq-key"  # Free at console.groq.com
export SCRUM_API_BASE_URL="https://api.groq.com/openai/v1"
scrum examples/IntentComputeBirthYear.scrum
```

📚 **Complete #INTENT Guide**: [Getting Started with Intents](docs/GETTING-STARTED-INTENTS.md) | [LLM Configuration](docs/LLM-CONFIGURATION.md)

## Hello World

A glance of the scrum programming language using the 'Hello World' example:

```SCRUM
#SPRINTGOAL Deliver our first Scrum program

EPIC "SampleStories"

    USER STORY "HelloWorld"

        #REVIEW Our first Scrum Program
        SAY "Hello world!"

    END OF STORY

END OF EPIC
```

### Hello World with #INTENT (AI-Powered)

Experience the future of programming with natural language:

```SCRUM
EPIC "HelloWorldApp"
    USER STORY "Greeting"
        #INTENT
        I want to create a simple greeting that displays "Hello from SCRUM!" to the user.
        #END INTENT
    END

    INSTANTIATE HelloWorldApp AS app
    app.Greeting
END
```

**The `#INTENT` block becomes real executable code automatically!** 🤖✨

## Running A Scrum Program

### Quick Start (SDK Installation)

**The recommended way to run SCRUM programs is to install the SDK:**

1. **Download** the latest SDK from [GitHub Releases](https://github.com/janvanwassenhove/scrum/releases)
2. **Install** using the provided installer script:
   - Windows: Run `installers\install.bat` (as Administrator)
   - Linux/macOS: Run `installers/install.sh` (with sudo for system-wide)
3. **Verify** installation: `scrum --version`
4. **Run** programs: `scrum examples/HelloWorld.scrum`

📚 **Complete installation guide**: See [SDK-INSTALLATION.md](docs/SDK-INSTALLATION.md)

### Running SCRUM code via unit test

Example code files can be found within [SCRUM Examples](development/examples).

The [SCRUM Examples](development/examples) can be executed and debugged using the [ScrumLanguageTest](test/scrum/ScrumLanguageTest.java).
The project is built upon Java 21 (LTS).

### Running from development build

To run SCRUM programs from the development directory (for contributors and developers):

Navigate to [Development](development) and run:
```bash
# Windows Command Prompt
scrum.bat examples\HelloWorld.scrum

# Windows PowerShell
.\scrum.bat examples\HelloWorld.scrum

# Linux/macOS
./scrum.sh examples/HelloWorld.scrum
```

You can use the [SCRUM Examples](development/examples) for testing purposes.

##### Local installation
If you want to run the code from anywhere, you can create a SCRUM_HOME variable and add it to the system varaiables.
Set the SCRUM_HOME environment variable pointing to your SCRUM installation and add this variable into your PATH variable adding %SCRUM_HOME%.

## IDE Support

SCRUM language extensions are available for popular IDEs to provide syntax highlighting, code folding, and language support.

### VS Code Extension

Get syntax highlighting and language support in Visual Studio Code:

**Quick Install (Windows):**
```powershell
cd ide-extensions
.\install-vscode.ps1
```

Or double-click `ide-extensions\install-vscode.bat` and restart VS Code.

**Features:**
- Syntax highlighting for keywords, directives, and operators
- Code folding for Sprint/Story/API blocks
- Auto-closing brackets and quotes
- Comment toggling (Ctrl+/)
- Bracket matching

### IntelliJ IDEA Plugin

Get SCRUM language support in IntelliJ IDEA:

**Build and Install:**
```powershell
cd ide-extensions
.\install-intellij.ps1
```

Then install the generated plugin via **Settings → Plugins → Install Plugin from Disk**.

**Features:**
- File type recognition for `.scrum` files
- Syntax highlighting
- Code structure view
- Custom file icon
- Comment support

For detailed installation instructions, manual setup, and development guides, see [IDE Extensions Documentation](ide-extensions/README.md).

## Language Reference

### Core Features

**Traditional Programming Constructs:**
- Business-friendly syntax using Scrum terminology (EPIC, USER STORY, etc.)
- Variables, operators, control flow with intuitive keywords
- HTTP REST API definitions with declarative syntax
- Comprehensive error handling with "Scrum Impediment" reporting

**Revolutionary AI-Powered Programming:**
- **#INTENT blocks**: Write natural language that becomes executable code
- **Multi-provider LLM support**: OpenAI, Anthropic, Groq, Ollama, and more
- **Automatic fallback chains**: Seamlessly switch providers for reliability
- **Compile-time transformation**: AI code generation with validation

For complete syntax documentation including operators, keywords, control structures, and data types, see the [Language Reference](docs/LANGUAGE-REFERENCE.md).

## API Definitions

SCRUM supports first-class HTTP API definitions using declarative syntax. See the complete [API Definitions documentation](docs/API-DEFINITIONS.md) for detailed information on:
- Defining APIs with base paths
- Creating endpoints with HTTP methods
- Executable endpoints with WHEN REQUEST blocks
- Complete API examples

## Error Handling

SCRUM features Scrum-inspired error reporting that transforms technical exceptions into narrative-style impediment messages using familiar Scrum terminology.

### Runtime Impediments

When a USER STORY encounters an error during execution, SCRUM reports it as an **IMPEDIMENT** that blocked the story from completing:

```
╔════════════════════════════════════════════════════════════════╗
║  SCRUM IMPEDIMENT – USER STORY COULD NOT BE COMPLETED          ║
╚════════════════════════════════════════════════════════════════╝

FILE DivisionByZero.scrum, LINE 3

DURING EXECUTION OF THIS STORY I TRIED TO:
    result IS a / b

BUT I ENCOUNTERED A BLOCKER:
    Division by zero is not allowed

IMPEDIMENT CODE: SCRUM-RUNTIME-ARITH-001
```

### Syntax Impediments

Parse and syntax errors are reported as impediments discovered during **BACKLOG REFINEMENT**:

```
╔════════════════════════════════════════════════════════════════╗
║  SCRUM IMPEDIMENT – BACKLOG ITEM NOT READY                     ║
╚════════════════════════════════════════════════════════════════╝

FILE billing.scrum, LINE 8, COLUMN 5

DURING BACKLOG REFINEMENT I COULD NOT UNDERSTAND THIS PART:
    I WANT TO DEFINE ENDPOINT "GetInvoices"

BECAUSE:
    Expected 'METHOD IS' after endpoint name

IMPEDIMENT CODE: SCRUM-SYNTAX-ENDPOINT-001
```

### Impediment Codes

SCRUM uses structured impediment codes for different error categories:

**Runtime Impediments:**
- `SCRUM-RUNTIME-ARITH-001` - Arithmetic errors (division by zero, overflow)
- `SCRUM-RUNTIME-NAME-001` - Undefined EPIC, USER STORY, or API references
- `SCRUM-RUNTIME-TYPE-001` - Type mismatch errors
- `SCRUM-RUNTIME-ITERATION-001` - Iteration errors (non-iterable values)
- `SCRUM-RUNTIME-PROPERTY-001` - Property access errors
- `SCRUM-RUNTIME-UNKNOWN-001` - Uncategorized runtime errors

**Syntax Impediments:**
- `SCRUM-SYNTAX-TOKEN-001` - Unrecognized tokens or characters
- `SCRUM-SYNTAX-STRUCTURE-001` - Misplaced or unfinished EPIC/USER STORY
- `SCRUM-SYNTAX-ENDPOINT-001` - Invalid API endpoint definition
- `SCRUM-SYNTAX-EXPRESSION-001` - Invalid expression or operator
- `SCRUM-SYNTAX-UNEXPECTED-001` - Unexpected token
- `SCRUM-SYNTAX-UNEXPECTED-001` - Unexpected token
- `SCRUM-SYNTAX-UNKNOWN-001` - Uncategorized syntax errors

### Debug Mode

For development and debugging, you can enable full Java stack traces alongside Scrum-style messages:

```bash
java -Dscrum.debug=true -jar scrum-language-1.2.0.jar yourfile.scrum
```

## Documentation

Complete documentation is available in the [`docs/`](docs/) directory:

### Core Documentation
- **[Language Reference](docs/LANGUAGE-REFERENCE.md)** - Complete SCRUM syntax and semantics
- **[API Definitions](docs/API-DEFINITIONS.md)** - REST API integration capabilities  
- **[SDK Installation Guide](docs/SDK-INSTALLATION.md)** - Install SCRUM globally on your system
- **[Quick Reference](docs/SDK-QUICK-REFERENCE.md)** - Command cheat sheet

### AI-Powered Programming (#INTENT)
- **[Getting Started with Intents](docs/GETTING-STARTED-INTENTS.md)** - Revolutionary natural language programming
- **[LLM Configuration](docs/LLM-CONFIGURATION.md)** - AI service setup and provider options
- **[Fallback Chain Implementation](docs/FALLBACK-CHAIN-IMPLEMENTATION.md)** - Multi-provider reliability

### For Contributors
- **[Release Process](docs/RELEASE-PROCESS.md)** - Step-by-step guide for releasing new SDK versions
- **[SDK Implementation](docs/SDK-IMPLEMENTATION.md)** - Technical architecture details

### For Contributors

If you're contributing to the SCRUM language:

- **[Release Process](docs/RELEASE-PROCESS.md)** - Step-by-step guide for releasing new SDK versions
- Use the automated release scripts: `release.bat`, `release.ps1`, or `release.sh`
- Follow semantic versioning (MAJOR.MINOR.PATCH)
- All releases are automatically published to GitHub via GitHub Actions

## Credits
The base code started from a sample project of @alexandermakeev [toy-language](https://github.com/alexandermakeev/toy-language).
A very fine starting base to get a good comprehension of developing a new programming language.
The original version of the SCRUM programming language was developed and designed by @janvanwassenhove (aka [mITy.John](www.mityjohn.com) - follow **mITy.John** on Instagram).

Check out the blog post on [mITy.John](www.mityjohn.com) for more information on the development of the SCRUM programming language or other projects.


<p align="center">
  <img src="asset/scrummy_duke.png" alt="Scrummy and Duke - Java Friendship" width="400"/>
</p>

Since SCRUM is built on top of Java, Scrummy and Duke (Java's mascot) are best friends! This friendship symbolizes how SCRUM leverages the power and stability of the Java platform while bringing its own unique, business-friendly syntax to the world of programming.

---

**Copyright (c) 2023-2025 Jan Van Wassenhove**  
Licensed under terms specified in [LICENSE](LICENSE) file.