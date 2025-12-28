package scrum.preprocessor.llm;

/**
 * Exception thrown when the LLM provider has exceeded its quota or credits.
 * This is a retryable error that suggests trying an alternative provider.
 */
public class QuotaExceededException extends LLMException {
    
    public QuotaExceededException(String message) {
        super(message);
    }
    
    public QuotaExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
