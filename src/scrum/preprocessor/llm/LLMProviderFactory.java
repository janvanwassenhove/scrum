package scrum.preprocessor.llm;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating LLM provider instances based on configuration.
 * Supports automatic provider selection with fallback.
 */
public class LLMProviderFactory {
    
    /**
     * Create an LLM provider instance based on the configured provider.
     * If "auto" is selected, tries providers in order: OpenAI-compatible API, then Ollama.
     * 
     * @return LLM provider instance
     * @throws IllegalArgumentException if the provider is not supported or none are available
     */
    public static LLMProvider createProvider() {
        String providerName = LLMConfig.getProvider();
        
        // Handle auto-detection
        if ("auto".equalsIgnoreCase(providerName)) {
            return createAutoProvider();
        }
        
        // Create specific provider
        return createProviderByName(providerName);
    }
    
    /**
     * Create a list of providers based on the fallback chain configuration.
     * Used by IntentPreprocessor to try multiple providers in sequence.
     * 
     * @return List of LLM providers in priority order
     */
    public static List<LLMProvider> createFallbackChain() {
        String chainConfig = LLMConfig.getFallbackChain();
        String[] providerNames = chainConfig.split(",");
        
        List<LLMProvider> providers = new ArrayList<>();
        for (String name : providerNames) {
            String trimmedName = name.trim();
            
            // Handle "auto" in the chain
            if ("auto".equalsIgnoreCase(trimmedName)) {
                LLMProvider autoProvider = createAutoProviderSilent();
                if (autoProvider != null) {
                    providers.add(autoProvider);
                }
            } else {
                try {
                    LLMProvider provider = createProviderByName(trimmedName);
                    if (provider != null && provider.isAvailable()) {
                        providers.add(provider);
                    }
                } catch (IllegalArgumentException e) {
                    // Skip invalid provider names in chain
                }
            }
        }
        
        if (providers.isEmpty()) {
            throw new IllegalStateException(
                "No LLM providers available in fallback chain: " + chainConfig + ". " +
                "Either:\n" +
                "  1. Set SCRUM_API_KEY for OpenAI/Anthropic/Groq/Cerebras/Together.ai, or\n" +
                "  2. Start Ollama with: ollama pull llama3.2\n" +
                "See documentation for configuration details."
            );
        }
        
        return providers;
    }
    
    /**
     * Create a provider by name.
     * 
     * @param providerName The provider name (api, openai, groq, anthropic, claude, ollama, etc.)
     * @return LLM provider instance
     * @throws IllegalArgumentException if provider name is not supported
     */
    public static LLMProvider createProviderByName(String providerName) {
        return switch (providerName.toLowerCase()) {
            case "api", "openai", "groq", "cerebras", "together" -> new OpenAICompatibleProvider();
            case "anthropic", "claude" -> new AnthropicProvider();
            case "ollama" -> new OllamaProvider();
            default -> throw new IllegalArgumentException("Unsupported LLM provider: " + providerName + 
                    ". Supported: auto, api, openai, groq, cerebras, together, anthropic, claude, ollama");
        };
    }
    
    /**
     * Automatically select an available provider.
     * Tries in order: OpenAI-compatible API (if key configured), then Ollama.
     */
    private static LLMProvider createAutoProvider() {
        LLMProvider provider = createAutoProviderSilent();
        if (provider != null) {
            return provider;
        }
        
        // No provider available
        throw new IllegalStateException(
                "No LLM provider available. Either:\n" +
                "  1. Set SCRUM_API_KEY for OpenAI/Anthropic/Groq/Cerebras/Together.ai, or\n" +
                "  2. Start Ollama with: ollama pull llama3.2\n" +
                "See documentation for configuration details."
        );
    }
    
    /**
     * Try to automatically select a provider without throwing exceptions.
     * Used internally by fallback chain logic.
     * 
     * @return LLM provider or null if none available
     */
    private static LLMProvider createAutoProviderSilent() {
        // Try API provider first if API key is configured
        String apiKey = LLMConfig.getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            // Auto-detect provider from API key prefix and configure defaults
            LLMProvider provider = detectAndConfigureProvider(apiKey);
            if (provider != null && provider.isAvailable()) {
                return provider;
            }
        }
        
        // Try Ollama as fallback
        OllamaProvider ollamaProvider = new OllamaProvider();
        if (ollamaProvider.isAvailable()) {
            return ollamaProvider;
        }
        
        return null;
    }
    
    /**
     * Auto-detect provider from API key prefix and set environment defaults if not configured.
     * Returns the appropriate provider instance.
     */
    private static LLMProvider detectAndConfigureProvider(String apiKey) {
        // Only set defaults if user hasn't explicitly configured
        String currentBaseUrl = System.getenv(LLMConfig.API_BASE_URL_ENV);
        String currentModel = System.getenv(LLMConfig.API_MODEL_ENV);
        
        boolean baseUrlNotSet = (currentBaseUrl == null || currentBaseUrl.isEmpty());
        boolean modelNotSet = (currentModel == null || currentModel.isEmpty());
        
        if (apiKey.startsWith("sk-ant-")) {
            // Anthropic key
            if (baseUrlNotSet) {
                System.setProperty("SCRUM_API_BASE_URL_DETECTED", "https://api.anthropic.com");
            }
            if (modelNotSet) {
                System.setProperty("SCRUM_API_MODEL_DETECTED", "claude-sonnet-4-20250514");
            }
            return new AnthropicProvider();
        } else if (apiKey.startsWith("sk-proj-") || apiKey.startsWith("sk-")) {
            // OpenAI key
            if (baseUrlNotSet) {
                System.setProperty("SCRUM_API_BASE_URL_DETECTED", "https://api.openai.com/v1");
            }
            if (modelNotSet) {
                System.setProperty("SCRUM_API_MODEL_DETECTED", "gpt-4o-mini");
            }
            return new OpenAICompatibleProvider();
        } else if (apiKey.startsWith("gsk_")) {
            // Groq key
            if (baseUrlNotSet) {
                System.setProperty("SCRUM_API_BASE_URL_DETECTED", "https://api.groq.com/openai/v1");
            }
            if (modelNotSet) {
                System.setProperty("SCRUM_API_MODEL_DETECTED", "llama-3.3-70b-versatile");
            }
            return new OpenAICompatibleProvider();
        }
        // For other providers (Cerebras, Together.ai), user must set base URL explicitly
        return new OpenAICompatibleProvider();
    }
}
