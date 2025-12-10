![SCRUM language](asset/banner.png)

# SCRUM
The home of the SCRUM programming language.
Designed for being a language which is understandable for business as well as for developers 
and losely inspired by the Scrum (software development) framework (hence the name of course).

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

## Running A Scrum Program
Example code files can be found within [SCRUM Examples](development/examples).

### Running SCRUM code via unit test.
The [SCRUM Examples](development/examples) can be executed and debugged using the [ScrumLanguageTest](test/java/com/mityjohn/scrumlanguage).
The project is build upon JAVA 21 (LTS).

### Running locally via command line
To run SCRUM programs locally, the scrum distributable can be used (make sure to have a recent java version installed). 
This can either be generated running a maven build (distribution will be generated within the /development directory), or directly downloading the scrum.zip distributable.

To run a program from the repository code, navigate to [Development](development) and run following code via CLI:
```
scrum examples/HelloWorld.scrum
```
You can use the [SCRUM Examples](development/examples) for testing purposes.
Keep in mind running this in Powershell you'll need to run it with:
```
.\scrum examples/HelloWorld.scrum
```

##### Local installation
If you want to run the code from anywhere, you can create a SCRUM_HOME variable and add it to the system varaiables.
Set the SCRUM_HOME environment variable pointing to your SCRUM installation and add this variable into your PATH variable adding %SCRUM_HOME%.

## Language Reference

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
- `SCRUM-SYNTAX-UNKNOWN-001` - Uncategorized syntax errors

### Debug Mode

For development and debugging, you can enable full Java stack traces alongside Scrum-style messages:

```bash
java -Dscrum.debug=true -jar scrum-language-1.2.0.jar yourfile.scrum
```

## Credits
The base code started from a sample project of @alexandermakeev [toy-language](https://github.com/alexandermakeev/toy-language).
A very fine starting base to get a good comprehension of developing a new programming language.
The original version of the SCRUM programming language was developed and designed by @janvanwassenhove (aka [mITy.John](www.mityjohn.com) - follow **mITy.John** on Instagram).

Check out the blog post on [mITy.John](www.mityjohn.com) for more information on the development of the SCRUM programming language or other projects.