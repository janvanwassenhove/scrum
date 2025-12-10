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

## Syntax

### File formatting
Scrum programs are [UTF-8](https://en.wikipedia.org/wiki/UTF-8) files with the **_.scrum_** file extension.

### Programming Structure
SCRUM code is a structure based upon EPIC's containing multiple USER STORIES or for simple coding purposes on single/multiple USER STORIES.
An EPIC can be interpreted as a single class, whereas User stories can be seen as single functions. 
These User Stories are individual blocks of code logic which can be executed individually.
Multiple stories within an epic will fulfil a bigger goal all together.

EPICS an USER STORIES have a name, defined between quotes (without spaces).
These epics can be called afterwards (without the spaces).

```SCRUM
EPIC "nameOfTheEpic"
    USER STORY "nameOfTheUS"
    
        ... code logic ...
        
    END OF USER STORY  
END OF EPIC
```
All code blocks (e.g. EPIC, USER STORY but also IF or ITERATIONS) are always closed by an END STATEMENT (combinining the initial parent).
So for an "EPIC" block, this will be closed by "END OF EPIC".

Initialising a new EPIC and calling an EPICs User Story:
```
SampleStoriesEpic IS NEW nameOfTheEpic
SampleStoriesEpic::nameOfTheUS USING []
```

### Operators & Variables declaration

All operators are written uppercase for easy readability (as user stories are written in a comprehensive way, we must make a distinction).

#### Plain types
Assigning a variable:
_variable name_ IS _expression_

```SCRUM
helloWorldText IS "Hello World!"
firstNumber IS 1
```

#### Array
BACKLOG = { <value1>, <value2>, ... }
example_backlog = { 1, 2, "three"}
empty_backlog = {}

### Operators

| Operator                 | Value        | Precedence | Example                         |
|--------------------------|--------------|------------|---------------------------------|
| Assignment               | ```IS```     | 1          | ```a = 5```                     |
| Append value to array    | ```ADDING``` | 1          | ```array ADDING "value"```      |
| Logical OR               | ```OR```     | 2          | ```true OR false```             |
| Logical AND              | ```AND```    | 3          | ```true AND true``` or ```"Hello" AND " World"``` |
| Left side                | ```(```      | 4          |                                 |
| Right side               | ```)```      | 4          |                                 |
| Equals                   | ```=```      | 5          | ```a = 5```                     |
| Not Equals               | ```!=```     | 5          | ```a != 5```                    |
| Greater Than Or Equals   | ```>=```     | 5          | ```a >= 5```                    |
| Greater Than             | ```>```      | 5          | ```a > 5```                     |
| Less Than Or Equals      | ```<=```     | 5          | ```a <= 5```                    |
| Less Than                | ```<```      | 5          | ```a < 5```                     |
| Addition                 | ```+```      | 6          | ```a + 5```                     |
| Subtraction              | ```-```      | 6          | ```a - 5```                     |
| Multiplication           | ```*```      | 7          | ```a * 5```                     |
| Division                 | ```/```      | 7          | ```a / 5```                     |
| Floor Division           | ```//```     | 7          | ```a // 5```                    |
| Modulo                   | ```%```      | 7          | ```a % 5```                     |
| NOT                      | ```!```      | 8          | ```!false```                    |
| EPIC Instance            | ```NEW```    | 8          | ```a IS NEW epicName [ 5 ]```   |
| EPIC Property            | ```::```     | 8          | ```epicName :: storyName```     |
| ------------------------ |--------------|------------| ------------------------------- |

### Keywords
#### Case sensitivity in SCRUM
Scrum keywords are case-sensitive.
All keywords are written uppercase for easy readability 
as user stories are written in a comprehensive way, we must make a distinction.

#### Print out to the console
```
SAY something
```

#### Retrieving input from console
```
ASK something
```

### Conditions
Example of condition statement:
```
    IF ..condition..
        ... DO SOMETHING ...
    ELSEIF ..condition..
        ... DO SOMETHING ...
    ELSE ..condition..
        ... DO SOMETHING ...  
    END IF
```

### Iterations
Example of iteration statement:
```
        I WANT TO ITERATE i FOR RANGE 0 TILL listLength 
            ... do something with i ...
        END OF ITERATION
```

## API Definitions

SCRUM supports first-class HTTP API definitions using declarative syntax that aligns with the narrative style of the language.

### Defining an API

Define an API with a base path:

```SCRUM
I WANT TO DEFINE API "InvoiceApi"
    BASE IS "/api/invoices"
END OF API
```

### Defining Endpoints

Endpoints can be nested within API blocks and support various HTTP methods and properties:

```SCRUM
I WANT TO DEFINE API "InvoiceApi"
    BASE IS "/api/invoices"

    I WANT TO DEFINE ENDPOINT "GetCustomerInvoices"
        METHOD IS "GET"
        PATH IS "/customer/{customerId}"
        QUERY_PARAMS ARE { "status", "year" }
        RETURNS IS "Invoice[]"
    END OF ENDPOINT

    I WANT TO DEFINE ENDPOINT "GetInvoiceById"
        METHOD IS "GET"
        PATH IS "/{invoiceId}"
        RETURNS IS "Invoice"
    END OF ENDPOINT

    I WANT TO DEFINE ENDPOINT "CreateInvoice"
        METHOD IS "POST"
        PATH IS "/create"
        RETURNS IS "Invoice"
    END OF ENDPOINT

END OF API
```

### Executable Endpoints with WHEN REQUEST

Endpoints can include executable handler logic using `WHEN REQUEST` blocks:

```SCRUM
I WANT TO DEFINE API "Greeting API"
    BASE IS "/api/v1/greetings"

    I WANT TO DEFINE ENDPOINT "Greet User"
        METHOD IS "GET"
        PATH IS "/hello/{name}"
        RETURNS IS "String"
        
        WHEN REQUEST
            SAY "┌─────────────────────────┐"
            SAY "│   Greeting Endpoint     │"
            SAY "├─────────────────────────┤"
            SAY "│ Endpoint was called!    │"
            SAY "└─────────────────────────┘"
        END WHEN
    END OF ENDPOINT

END OF API
```

The `WHEN REQUEST` block can contain any valid SCRUM statements, including:
- Variable assignments
- Function calls
- Conditional logic (IF/ELSE)
- Loops (I WANT TO ITERATE)
- Output statements (SAY)

### API Keywords

| Keyword         | Purpose                                          | Example                          |
|-----------------|--------------------------------------------------|----------------------------------|
| `I WANT TO DEFINE` | Introduces an API or endpoint definition      | `I WANT TO DEFINE API "UserApi"` |
| `API`           | Declares an API block                            | `I WANT TO DEFINE API "UserApi"` |
| `ENDPOINT`      | Declares an endpoint within an API               | `I WANT TO DEFINE ENDPOINT "GetUsers"` |
| `BASE`          | Specifies the base path for an API               | `BASE IS "/api/users"`           |
| `METHOD`        | Specifies the HTTP method for an endpoint        | `METHOD IS "GET"`                |
| `PATH`          | Specifies the URL path for an endpoint           | `PATH IS "/users/{id}"`          |
| `QUERY_PARAMS`  | Declares query parameters for an endpoint        | `QUERY_PARAMS ARE { "page", "limit" }` |
| `RETURNS`       | Specifies the return type of an endpoint         | `RETURNS IS "User[]"`            |
| `WHEN`          | Marks the beginning of a request handler block   | `WHEN REQUEST`                   |
| `REQUEST`       | Follows WHEN to indicate request handling        | `WHEN REQUEST`                   |
| `END WHEN`      | Closes a WHEN REQUEST block                      | `END WHEN`                       |
| `END OF API`    | Closes an API definition block                   | `END OF API`                     |
| `END OF ENDPOINT` | Closes an endpoint definition block            | `END OF ENDPOINT`                |

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

### Complete API Examples

See these examples for complete working demonstrations:
- [development/examples/ApiExample.scrum](development/examples/ApiExample.scrum) - Multiple API definitions with various HTTP methods
- [development/examples/ExecutableApiExample.scrum](development/examples/ExecutableApiExample.scrum) - Executable endpoints with WHEN REQUEST blocks
- [development/examples/AgeCalculatorApi.scrum](development/examples/AgeCalculatorApi.scrum) - Interactive API with user input
- [development/examples/SimpleAgeApi.scrum](development/examples/SimpleAgeApi.scrum) - Simple age calculator with formatted output

## Credits
The base code started from a sample project of @alexandermakeev [toy-language](https://github.com/alexandermakeev/toy-language).
A very fine starting base to get a good comprehension of developing a new programming language.
The original version of the SCRUM programming language was developed and designed by @janvanwassenhove (aka [mITy.John](www.mityjohn.com) - follow **mITy.John** on Instagram).

Check out the blog post on [mITy.John](www.mityjohn.com) for more information on the development of the SCRUM programming language or other projects.