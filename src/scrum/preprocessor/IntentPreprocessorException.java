package scrum.preprocessor;

/**
 * Exception thrown when intent preprocessing fails.
 */
public class IntentPreprocessorException extends Exception {
    
    public IntentPreprocessorException(String message) {
        super(message);
    }
    
    public IntentPreprocessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
