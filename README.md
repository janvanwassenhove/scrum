![SCRUM language](asset/banner.png)

# SCRUM
The home of the SCRUM programming language.
Designed for being a language which is understandable for business as well as for developers 
and losely inspired by the Scrum (software development) framework (hence the name of course).

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
| Logical AND              | ```AND```    | 3          | ```true AND true```             |
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

## Credits
The base code started from a sample project of @alexandermakeev [toy-language](https://github.com/alexandermakeev/toy-language).
A very fine starting base to get a good comprehension of developing a new programming language.
The original version of the SCRUM programming language was developed and designed by @janvanwassenhove (aka [mITy.John](www.mityjohn.com) - follow **mITy.John** on Instagram).

Check out the blog post on [mITy.John](www.mityjohn.com) for more information on the development of the SCRUM programming language or other projects.