package scrum.preprocessor.llm;

/**
 * Configuration for LLM providers.
 * Reads from environment variables with sensible defaults.
 */
public class LLMConfig {
    
    // Provider selection
    private static final String DEFAULT_PROVIDER = "auto";
    public static final String PROVIDER_ENV = "SCRUM_LLM_PROVIDER";
    
    // Ollama configuration
    private static final String DEFAULT_OLLAMA_URL = "http://localhost:11434";
    private static final String DEFAULT_OLLAMA_MODEL = "llama3.2";
    private static final double DEFAULT_OLLAMA_TEMPERATURE = 0.3;
    
    public static final String OLLAMA_URL_ENV = "SCRUM_OLLAMA_URL";
    public static final String OLLAMA_MODEL_ENV = "SCRUM_OLLAMA_MODEL";
    public static final String OLLAMA_TEMPERATURE_ENV = "SCRUM_OLLAMA_TEMPERATURE";
    
    // OpenAI-compatible API configuration
    private static final String DEFAULT_API_BASE_URL = "https://api.openai.com/v1";
    private static final String DEFAULT_API_MODEL = "gpt-4o-mini";
    private static final double DEFAULT_API_TEMPERATURE = 0.3;
    
    public static final String API_KEY_ENV = "SCRUM_API_KEY";
    public static final String API_BASE_URL_ENV = "SCRUM_API_BASE_URL";
    public static final String API_MODEL_ENV = "SCRUM_API_MODEL";
    public static final String API_TEMPERATURE_ENV = "SCRUM_API_TEMPERATURE";
    
    // Timeout configuration (in seconds)
    private static final int DEFAULT_LLM_TIMEOUT = 120; // 2 minutes
    public static final String LLM_TIMEOUT_ENV = "SCRUM_LLM_TIMEOUT";
    
    // Fallback chain configuration
    private static final String DEFAULT_FALLBACK_CHAIN = "auto,groq,ollama";
    public static final String FALLBACK_CHAIN_ENV = "SCRUM_LLM_FALLBACK_CHAIN";
    
    /**
     * Get the configured LLM provider name.
     */
    public static String getProvider() {
        return System.getenv().getOrDefault(PROVIDER_ENV, DEFAULT_PROVIDER);
    }
    
    /**
     * Get the fallback chain of provider names.
     * Returns a comma-separated list like "auto,groq,ollama".
     */
    public static String getFallbackChain() {
        return System.getenv().getOrDefault(FALLBACK_CHAIN_ENV, DEFAULT_FALLBACK_CHAIN);
    }
    
    /**
     * Get the Ollama API URL.
     */
    public static String getOllamaUrl() {
        return System.getenv().getOrDefault(OLLAMA_URL_ENV, DEFAULT_OLLAMA_URL);
    }
    
    /**
     * Get the Ollama model name.
     */
    public static String getOllamaModel() {
        return System.getenv().getOrDefault(OLLAMA_MODEL_ENV, DEFAULT_OLLAMA_MODEL);
    }
    
    /**
     * Get the Ollama temperature setting for code generation.
     */
    public static double getOllamaTemperature() {
        String temp = System.getenv().get(OLLAMA_TEMPERATURE_ENV);
        if (temp != null) {
            try {
                return Double.parseDouble(temp);
            } catch (NumberFormatException e) {
                // Fall back to default
            }
        }
        return DEFAULT_OLLAMA_TEMPERATURE;
    }
    
    /**
     * Get the API key for OpenAI-compatible providers.
     */
    public static String getApiKey() {
        return System.getenv().get(API_KEY_ENV);
    }
    
    /**
     * Get the API base URL for OpenAI-compatible providers.
     */
    public static String getApiBaseUrl() {
        String envValue = System.getenv(API_BASE_URL_ENV);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        // Check for auto-detected value
        String detected = System.getProperty("SCRUM_API_BASE_URL_DETECTED");
        return detected != null ? detected : DEFAULT_API_BASE_URL;
    }
    
    /**
     * Get the API model name.
     */
    public static String getApiModel() {
        String envValue = System.getenv(API_MODEL_ENV);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        // Check for auto-detected value
        String detected = System.getProperty("SCRUM_API_MODEL_DETECTED");
        return detected != null ? detected : DEFAULT_API_MODEL;
    }
    
    /**
     * Get the API temperature setting for code generation.
     */
    public static double getApiTemperature() {
        String temp = System.getenv().get(API_TEMPERATURE_ENV);
        if (temp != null) {
            try {
                return Double.parseDouble(temp);
            } catch (NumberFormatException e) {
                // Fall back to default
            }
        }
        return DEFAULT_API_TEMPERATURE;
    }
    
    /**
     * Get the timeout in seconds for LLM requests.
     */
    public static int getLLMTimeout() {
        String timeout = System.getenv().get(LLM_TIMEOUT_ENV);
        if (timeout != null) {
            try {
                return Integer.parseInt(timeout);
            } catch (NumberFormatException e) {
                // Fall back to default
            }
        }
        return DEFAULT_LLM_TIMEOUT;
    }
}
