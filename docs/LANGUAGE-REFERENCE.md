# SCRUM Language Reference

Complete syntax and language features reference for the SCRUM programming language.

## File Formatting

Scrum programs are [UTF-8](https://en.wikipedia.org/wiki/UTF-8) files with the **_.scrum_** file extension.

## Programming Structure

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
```SCRUM
SampleStoriesEpic IS NEW nameOfTheEpic
SampleStoriesEpic::nameOfTheUS USING []
```

## Variables and Data Types

### Plain Types

Assigning a variable:  
_variable name_ IS _expression_

```SCRUM
helloWorldText IS "Hello World!"
firstNumber IS 1
```

### Arrays (Backlogs)

Arrays in SCRUM are called BACKLOGS:

```SCRUM
BACKLOG = { <value1>, <value2>, ... }
example_backlog = { 1, 2, "three"}
empty_backlog = {}
```

## Operators

| Operator                 | Value        | Precedence | Example                         |
|--------------------------|--------------|------------|---------------------------------|
| Assignment               | ```IS```     | 1          | ```a IS 5```                    |
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

## Keywords

### Case Sensitivity

Scrum keywords are case-sensitive.
All keywords are written uppercase for easy readability 
as user stories are written in a comprehensive way, we must make a distinction.

### Input/Output

#### Print to Console
```SCRUM
SAY something
```

#### Retrieve Input from Console
```SCRUM
ASK something
```

## Control Structures

### Conditions

Example of condition statement:

```SCRUM
IF ..condition..
    ... DO SOMETHING ...
ELSEIF ..condition..
    ... DO SOMETHING ...
ELSE
    ... DO SOMETHING ...  
END IF
```

### Iterations

Example of iteration statement:

```SCRUM
I WANT TO ITERATE i FOR RANGE 0 TILL listLength 
    ... do something with i ...
END OF ITERATION
```

## Comments

### Sprint Goal Comments
```SCRUM
#SPRINTGOAL Deliver our first Scrum program
```

### Review Comments
```SCRUM
#REVIEW Our first Scrum Program
```

## Complete Example

```SCRUM
#SPRINTGOAL Deliver our first Scrum program

EPIC "SampleStories"

    USER STORY "HelloWorld"

        #REVIEW Our first Scrum Program
        SAY "Hello world!"

    END OF STORY

END OF EPIC
```
