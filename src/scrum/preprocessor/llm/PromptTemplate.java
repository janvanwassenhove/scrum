package scrum.preprocessor.llm;

/**
 * Prompt templates for LLM code generation.
 * Contains SCRUM language context and curated examples.
 */
public class PromptTemplate {
    
    /**
     * Build a complete prompt for translating an intent into SCRUM code.
     * 
     * @param intent The natural language intent
     * @return Complete prompt with context and examples
     */
    public static String buildPrompt(String intent) {
        return String.format("""
            You are a code generator for the SCRUM programming language. Generate ONLY valid SCRUM code based on the intent described below. Do not include explanations, comments, or markdown formatting.
            
            SCRUM Language Syntax:
            - EPIC blocks (classes): EPIC "Name" ... END OF EPIC
            - USER STORY blocks (functions): USER STORY "Name" ... END OF STORY
            - Print output: SAY "text" or SAY variable
            - Get user input: ASK "prompt" AS variableName
            - Variable assignment: variableName IS expression
            - Arrays (BACKLOGS): myList IS { value1, value2, value3 }
            - Add to array: myList ADDING newValue
            - Arithmetic: +, -, *, /, //, %
            - String concatenation: str1 AND str2
            - Comparison: =, !=, <, >, <=, >=
            - Logical: AND, OR, !
            - Conditionals: IF condition ... ELSEIF condition ... ELSE ... END IF
            - Loops: I WANT TO ITERATE i FOR RANGE start TILL end ... END OF ITERATION
            - Instantiate EPIC: instanceName IS NEW EpicName
            - Call USER STORY: instanceName::StoryName USING []
            - API definitions: I WANT TO DEFINE API "Name" BASE IS "/path" ... END OF API
            - Endpoints: I WANT TO DEFINE ENDPOINT "Name" METHOD IS "GET/POST" PATH IS "/path" ... END OF ENDPOINT
            
            Examples:
            
            Example 1 - User Input and Calculation:
            Intent: Ask the user for their age and calculate their birth year
            SCRUM Code:
            USER STORY "CalculateBirthYear"
                ASK "What is your age?" AS age
                currentYear IS 2024
                birthYear IS currentYear - age
                SAY "You were born in "
                SAY birthYear
            END OF STORY
            
            Example 2 - Conditional Logic:
            Intent: Ask for a number and tell if it's positive, negative, or zero
            SCRUM Code:
            USER STORY "CheckNumber"
                ASK "Enter a number:" AS num
                IF num > 0
                    SAY "The number is positive"
                ELSEIF num < 0
                    SAY "The number is negative"
                ELSE
                    SAY "The number is zero"
                END IF
            END OF STORY
            
            Example 3 - Simple API Definition:
            Intent: Create an API endpoint that returns a greeting message
            SCRUM Code:
            USER STORY "GreetingApi"
                I WANT TO DEFINE API "GreetingApi"
                    BASE IS "/api/greet"
                    
                    I WANT TO DEFINE ENDPOINT "SayHello"
                        METHOD IS "GET"
                        PATH IS "/hello/{name}"
                        RETURNS IS "String"
                    END OF ENDPOINT
                END OF API
                
                SAY "Greeting API defined successfully"
            END OF STORY
            
            Example 4 - API with Calculation:
            Intent: Create an API that calculates age from birth year
            SCRUM Code:
            USER STORY "AgeCalculatorApi"
                I WANT TO DEFINE API "AgeApi"
                    BASE IS "/api/age"
                    
                    I WANT TO DEFINE ENDPOINT "CalculateAge"
                        METHOD IS "GET"
                        PATH IS "/calculate/{birthYear}"
                        QUERY_PARAMS ARE { "currentYear" }
                        RETURNS IS "Integer"
                    END OF ENDPOINT
                END OF API
                
                SAY "Age Calculator API defined"
            END OF STORY
            
            Example 5 - Multiple Statements:
            Intent: Greet the user, ask their name, and respond with a personalized message
            SCRUM Code:
            USER STORY "PersonalGreeting"
                SAY "Welcome to SCRUM!"
                ASK "What is your name?" AS userName
                SAY "Hello, "
                SAY userName
                SAY "! Nice to meet you."
            END OF STORY
            
            Example 6 - Arrays and Iteration:
            Intent: Create a list of numbers from 1 to 5 and print each one
            SCRUM Code:
            USER STORY "PrintNumbers"
                numbers IS { 1, 2, 3, 4, 5 }
                I WANT TO ITERATE i FOR RANGE 0 TILL 5
                    SAY "Number: "
                    SAY numbers[i]
                END OF ITERATION
            END OF STORY
            
            Example 7 - EPIC with Multiple USER STORIES:
            Intent: Create an EPIC with a user story that calculates and displays a total
            SCRUM Code:
            EPIC "Calculator"
                USER STORY "AddNumbers"
                    total IS 0
                    I WANT TO ITERATE i FOR RANGE 1 TILL 11
                        total IS total + i
                    END OF ITERATION
                    SAY "Total is: "
                    SAY total
                END OF STORY
            END OF EPIC
            
            Now generate SCRUM code for this intent:
            %s
            
            Remember: Output ONLY the SCRUM code, nothing else. Start with USER STORY (or EPIC if needed) and end with END OF STORY (or END OF EPIC).
            """, intent);
    }
}
