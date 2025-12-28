package scrum.preprocessor;

import scrum.LexicalParser;
import scrum.StatementParser;
import scrum.exception.SyntaxException;
import scrum.preprocessor.llm.LLMException;
import scrum.preprocessor.llm.LLMProvider;
import scrum.preprocessor.llm.LLMProviderFactory;
import scrum.preprocessor.llm.QuotaExceededException;
import scrum.preprocessor.llm.RateLimitException;
import scrum.statement.CompositeStatement;
import scrum.statement.IntentBlockStatement;
import scrum.statement.Statement;
import scrum.token.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Preprocessor that transforms #INTENT blocks into executable SCRUM code.
 * Uses LLM to translate natural language intents into proper SCRUM statements.
 * Supports fallback to alternative providers when quota or rate limits are exceeded.
 */
public class IntentPreprocessor {
    
    private static final int MAX_RETRIES = 3;
    private final List<LLMProvider> providerChain;
    private final Set<String> failedProviders;
    
    public IntentPreprocessor() {
        this.providerChain = LLMProviderFactory.createFallbackChain();
        this.failedProviders = new HashSet<>();
    }
    
    /**
     * Constructor for testing with a custom LLM provider.
     */
    public IntentPreprocessor(LLMProvider llmProvider) {
        this.providerChain = List.of(llmProvider);
        this.failedProviders = new HashSet<>();
    }
    
    /**
     * Process all intent blocks in the given statement tree.
     * Transforms IntentBlockStatement nodes by calling LLM and reparsing the generated code.
     * 
     * @param statement The root statement to process
     * @throws IntentPreprocessorException if preprocessing fails
     */
    public void processIntents(CompositeStatement statement) throws IntentPreprocessorException {
        transformIntents(statement);
    }
    
    /**
     * Recursively traverse the AST and transform all IntentBlockStatement nodes.
     */
    private void transformIntents(CompositeStatement compositeStatement) throws IntentPreprocessorException {
        List<Statement> statements = compositeStatement.getStatements2Execute();
        
        for (int i = 0; i < statements.size(); i++) {
            Statement stmt = statements.get(i);
            
            if (stmt instanceof IntentBlockStatement) {
                IntentBlockStatement intentStmt = (IntentBlockStatement) stmt;
                
                // Transform this intent block
                transformIntentBlock(intentStmt);
                
            } else if (stmt instanceof CompositeStatement) {
                // Recursively process nested composite statements
                transformIntents((CompositeStatement) stmt);
            }
        }
    }
    
    /**
     * Transform a single intent block by generating code and replacing its contents.
     * Tries multiple providers in fallback chain if quota or rate limits are hit.
     */
    private void transformIntentBlock(IntentBlockStatement intentBlock) throws IntentPreprocessorException {
        String intent = intentBlock.getRawIntent();
        String generatedCode = null;
        String lastError = null;
        
        // Try each provider in the fallback chain
        for (LLMProvider provider : providerChain) {
            String providerName = provider.getProviderName();
            
            // Skip providers that have already failed with quota/rate limit issues
            if (failedProviders.contains(providerName)) {
                continue;
            }
            
            // Try up to MAX_RETRIES times with feedback on this provider
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    // Generate SCRUM code from the intent
                    if (attempt == 1) {
                        generatedCode = provider.generateCode(intent);
                    } else {
                        // On retry, include error feedback in the prompt
                        String feedbackPrompt = intent + "\n\nPrevious attempt generated invalid SCRUM code. Error: " + 
                                lastError + "\nPlease correct the syntax and try again.";
                        generatedCode = provider.generateCode(feedbackPrompt);
                    }
                    
                    // Parse the generated code
                    List<Statement> parsedStatements = parseGeneratedCode(generatedCode);
                    
                    // Replace the intent block's statements with the generated ones
                    intentBlock.getStatements2Execute().clear();
                    for (Statement stmt : parsedStatements) {
                        intentBlock.addStatement(stmt);
                    }
                    
                    // Mark as successfully generated
                    intentBlock.setGenerated(true);
                    
                    // Success - exit completely
                    return;
                    
                } catch (QuotaExceededException | RateLimitException e) {
                    // Mark this provider as failed and try the next one in the chain
                    failedProviders.add(providerName);
                    System.err.println("Provider " + providerName + " failed with: " + e.getMessage());
                    System.err.println("Trying next provider in fallback chain...");
                    break; // Break retry loop, move to next provider
                    
                } catch (LLMException e) {
                    // Other LLM errors (network, timeout, etc.) - fail immediately
                    throw new IntentPreprocessorException(
                        "Failed to generate code from LLM (" + providerName + ") for intent: " + 
                        intent.substring(0, Math.min(100, intent.length())) + "...",
                        e
                    );
                } catch (SyntaxException e) {
                    lastError = e.getMessage();
                    
                    if (attempt == MAX_RETRIES) {
                        // Final retry attempt failed - try next provider
                        System.err.println("Provider " + providerName + " failed after " + MAX_RETRIES + 
                                " attempts to generate valid syntax. Trying next provider...");
                        break; // Break retry loop, move to next provider
                    }
                    
                    // Continue to next retry attempt with same provider
                }
            }
        }
        
        // All providers exhausted
        throw new IntentPreprocessorException(
            "Unable to translate intent to valid SCRUM code after trying all providers in fallback chain.\n" +
            "Intent: " + intent.substring(0, Math.min(200, intent.length())) + 
            (intent.length() > 200 ? "..." : "") + "\n" +
            "Last error: " + lastError + "\n" +
            "Failed providers: " + failedProviders + "\n" +
            "Configure additional providers with SCRUM_LLM_FALLBACK_CHAIN."
        );
    }
    
    /**
     * Parse generated SCRUM code into a list of statements.
     * Expects the code to be a complete USER STORY block or sequence of statements.
     */
    private List<Statement> parseGeneratedCode(String code) throws SyntaxException {
        // Tokenize the generated code
        LexicalParser lexer = new LexicalParser(code);
        List<Token> tokens = lexer.parse();
        
        // Parse into statements
        CompositeStatement tempStatement = new CompositeStatement();
        StatementParser.parse(tokens, tempStatement);
        
        return tempStatement.getStatements2Execute();
    }
    
    /**
     * Check if at least one LLM provider is available before preprocessing.
     * 
     * @return true if at least one provider is ready to use
     */
    public boolean isLLMAvailable() {
        return !providerChain.isEmpty() && providerChain.stream().anyMatch(LLMProvider::isAvailable);
    }
}
