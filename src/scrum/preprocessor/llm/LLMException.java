package scrum.preprocessor.llm;

/**
 * Exception thrown when LLM code generation fails.
 */
public class LLMException extends Exception {
    
    public LLMException(String message) {
        super(message);
    }
    
    public LLMException(String message, Throwable cause) {
        super(message, cause);
    }
}
