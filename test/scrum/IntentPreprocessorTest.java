package scrum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrum.context.definition.DefinitionContext;
import scrum.context.definition.FunctionDefinition;
import scrum.context.MemoryContext;
import scrum.exception.TokenException;
import scrum.preprocessor.IntentPreprocessor;
import scrum.preprocessor.IntentPreprocessorException;
import scrum.preprocessor.llm.LLMException;
import scrum.preprocessor.llm.LLMProvider;
import scrum.statement.CompositeStatement;
import scrum.statement.FunctionStatement;
import scrum.statement.IntentBlockStatement;
import scrum.token.Token;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Intent preprocessing functionality with mocked LLM provider.
 */
class IntentPreprocessorTest {

    @BeforeEach
    public void setUp() {
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
    }

    @AfterEach
    public void tearDown() {
        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

    @Test
    public void testIntentBlockStatementParsing() {
        String source = """
            USER STORY "TestIntent"
                #INTENT
                Ask the user for their age and calculate birth year
                #END INTENT
            END OF STORY
            """;

        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();

        CompositeStatement program = new CompositeStatement();
        assertDoesNotThrow(() -> StatementParser.parse(tokens, program));

        // USER STORY creates a function definition in DefinitionContext, not a statement in program
        // So we need to get the function definition and check its content
        FunctionDefinition func = DefinitionContext.getScope().getFunction("TestIntent");
        assertNotNull(func, "TestIntent function should be defined");

        // The function statement should contain an IntentBlockStatement
        FunctionStatement funcStmt = func.getStatement();
        assertTrue(funcStmt.getStatements2Execute().stream()
                .anyMatch(s -> s instanceof IntentBlockStatement),
                "Function should contain an IntentBlockStatement");

        // Get the intent block
        IntentBlockStatement intentBlock = (IntentBlockStatement) funcStmt.getStatements2Execute().stream()
                .filter(s -> s instanceof IntentBlockStatement)
                .findFirst()
                .orElseThrow();

        String rawIntent = intentBlock.getRawIntent();
        assertTrue(rawIntent.contains("age"), "Intent should contain 'age'");
        assertTrue(rawIntent.contains("birth year"), "Intent should contain 'birth year'");
        assertFalse(intentBlock.isGenerated(), "Intent should not be marked as generated yet");
    }

    @Test
    public void testPreprocessorWithMockedLLM() throws IntentPreprocessorException {
        // Create a mock LLM provider
        LLMProvider mockProvider = new LLMProvider() {
            @Override
            public String generateCode(String intent) throws LLMException {
                return """
                    SAY "Generated from intent"
                    """;
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public String getProviderName() {
                return "mock";
            }
        };

        // Parse a program with an intent block
        String source = """
            USER STORY "TestStory"
                #INTENT
                Say hello to the user
                #END INTENT
            END OF STORY
            """;

        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();

        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);

        // Get the function to process its intents
        FunctionDefinition func = DefinitionContext.getScope().getFunction("TestStory");
        assertNotNull(func);
        
        FunctionStatement funcStmt = func.getStatement();

        // Process intents with mocked provider
        IntentPreprocessor preprocessor = new IntentPreprocessor(mockProvider);
        preprocessor.processIntents(funcStmt);  // Process the function statement directly

        // Get the intent block
        IntentBlockStatement intentBlock = (IntentBlockStatement) funcStmt.getStatements2Execute().stream()
                .filter(s -> s instanceof IntentBlockStatement)
                .findFirst()
                .orElseThrow();

        assertTrue(intentBlock.isGenerated(), "Intent should be marked as generated");
        assertTrue(intentBlock.getStatements2Execute().size() > 0, "Intent should have generated statements");
    }

    @Test
    public void testEmptyIntentBlock() throws Exception {
        String source = """
            USER STORY "TestEmpty"
                #INTENT
                #END INTENT
            END OF STORY
            """;

        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        // Empty intent text is allowed at parse level (the lexer skips empty text)
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        // No exception should be thrown - empty intent blocks are simply skipped
    }

    @Test
    public void testMissingEndIntent() {
        String source = """
            USER STORY "TestMissing"
                #INTENT
                Some intent text
            END OF STORY
            """;

        LexicalParser lexer = new LexicalParser(source);
        
        // Missing #END INTENT should throw TokenException during lexing
        assertThrows(TokenException.class, () -> lexer.parse(),
                "Missing #END INTENT should throw TokenException");
    }

    @Test
    public void testIntentBlockNotProcessed() {
        String source = """
            USER STORY "TestNotProcessed"
                #INTENT
                Test intent
                #END INTENT
            END OF STORY
            """;

        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();

        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);

        // Get the function and try to execute it without preprocessing - should throw error
        FunctionDefinition func = DefinitionContext.getScope().getFunction("TestNotProcessed");
        assertNotNull(func);
        
        FunctionStatement funcStmt = func.getStatement();
        assertThrows(RuntimeException.class, () -> funcStmt.execute(),
                "Executing unprocessed intent block should throw an exception");
    }
}
