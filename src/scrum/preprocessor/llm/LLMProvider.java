package scrum.preprocessor.llm;

/**
 * Interface for LLM providers that can translate natural language intents into SCRUM code.
 * This abstraction allows for multiple LLM backends (Ollama, OpenAI, llama.cpp, etc.)
 */
public interface LLMProvider {
    
    /**
     * Translate a natural language intent into executable SCRUM code.
     * 
     * @param intent The natural language description of what the code should do
     * @return Generated SCRUM code as a string
     * @throws LLMException if code generation fails
     */
    String generateCode(String intent) throws LLMException;
    
    /**
     * Check if the LLM provider is available and ready to use.
     * 
     * @return true if the provider is reachable and configured correctly
     */
    boolean isAvailable();
    
    /**
     * Get the name of this LLM provider.
     * 
     * @return Provider name (e.g., "ollama", "openai")
     */
    String getProviderName();
}
