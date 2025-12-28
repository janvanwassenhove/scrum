package scrum;

import lombok.SneakyThrows;
import scrum.context.ExecutionContext;
import scrum.context.MemoryContext;
import scrum.context.definition.DefinitionContext;
import scrum.preprocessor.IntentPreprocessor;
import scrum.preprocessor.IntentPreprocessorException;
import scrum.statement.CompositeStatement;
import scrum.token.Token;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScrumLanguage {

    @SneakyThrows
    public void execute(Path path) {
        String source = Files.readString(path);
        String fileName = path.getFileName().toString();
        
        // Initialize execution context
        ExecutionContext.initialize(fileName, source);
        
        try {
            LexicalParser lexicalParser = new LexicalParser(source);
            List<Token> tokens = lexicalParser.parse();

            DefinitionContext.pushScope(DefinitionContext.newScope());
            MemoryContext.pushScope(MemoryContext.newScope());
            try {
                CompositeStatement statement = new CompositeStatement();
                StatementParser.parse(tokens, statement);
                
                // Preprocess intent blocks before execution
                preprocessIntents(statement);
                
                statement.execute();
            } finally {
                DefinitionContext.endScope();
                MemoryContext.endScope();
            }
        } finally {
            ExecutionContext.clear();
        }
    }
    
    /**
     * Preprocess intent blocks in the parsed AST.
     * Transforms natural language intents into executable SCRUM code using LLM.
     */
    private void preprocessIntents(CompositeStatement statement) throws IntentPreprocessorException {
        // Check if there are any intent blocks in statements OR in function definitions
        boolean hasIntents = containsIntentBlocks(statement) || containsIntentBlocksInDefinitions();
        
        if (!hasIntents) {
            // No intent blocks, skip preprocessing entirely
            return;
        }
        
        // Intent blocks found - initialize preprocessor and LLM
        IntentPreprocessor preprocessor = new IntentPreprocessor();
        
        // Check if LLM is available
        if (!preprocessor.isLLMAvailable()) {
            throw new IntentPreprocessorException(
                "Intent blocks found but LLM is not available.\n" +
                "Either:\n" +
                "  1. Set SCRUM_API_KEY for OpenAI/Groq/Cerebras/Together.ai, or\n" +
                "  2. Start Ollama with: ollama pull llama3.2\n" +
                "See docs/LLM-CONFIGURATION.md for details."
            );
        }
        
        // Process all intent blocks in statements
        preprocessor.processIntents(statement);
        
        // Process intent blocks in function definitions (USER STORYs)
        preprocessFunctionDefinitions(preprocessor);
    }
    
    /**
     * Check if any function definitions contain intent blocks.
     */
    private boolean containsIntentBlocksInDefinitions() {
        var allFunctions = scrum.context.definition.DefinitionContext.getScope().getAllFunctionsRecursive();
        for (var func : allFunctions) {
            if (containsIntentBlocks(func.getStatement())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Preprocess intent blocks in all function definitions.
     */
    private void preprocessFunctionDefinitions(IntentPreprocessor preprocessor) throws IntentPreprocessorException {
        for (scrum.context.definition.FunctionDefinition func : scrum.context.definition.DefinitionContext.getScope().getAllFunctionsRecursive()) {
            preprocessor.processIntents(func.getStatement());
        }
    }
    
    /**
     * Check if the statement tree contains any IntentBlockStatement nodes.
     */
    private boolean containsIntentBlocks(CompositeStatement statement) {
        return statement.getStatements2Execute().stream()
            .anyMatch(stmt -> {
                if (stmt instanceof scrum.statement.IntentBlockStatement) {
                    return true;
                } else if (stmt instanceof CompositeStatement) {
                    return containsIntentBlocks((CompositeStatement) stmt);
                }
                return false;
            });
    }
}