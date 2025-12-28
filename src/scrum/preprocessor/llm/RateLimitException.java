package scrum.preprocessor.llm;

/**
 * Exception thrown when the LLM provider has rate limited the request.
 * This is a retryable error that suggests trying an alternative provider.
 */
public class RateLimitException extends LLMException {
    
    public RateLimitException(String message) {
        super(message);
    }
    
    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
