package scrum.preprocessor.llm;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Anthropic Claude API provider.
 * Supports Claude Sonnet 4, Claude Sonnet 4.5, and other Claude models.
 * 
 * Configuration via environment variables:
 * - SCRUM_API_KEY: Anthropic API key (starts with "sk-ant-")
 * - SCRUM_API_BASE_URL: Base URL (default: https://api.anthropic.com)
 * - SCRUM_API_MODEL: Model name (default: claude-sonnet-4-20250514)
 * - SCRUM_API_TEMPERATURE: Temperature (default: 0.3)
 * 
 * Supported models:
 * - claude-sonnet-4-20250514 (Claude Sonnet 4)
 * - claude-sonnet-4.5-20250514 (Claude Sonnet 4.5)
 * - claude-opus-4-20250514 (Claude Opus 4)
 */
public class AnthropicProvider implements LLMProvider {
    
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    
    private final String apiKey;
    private final String baseUrl;
    private final String model;
    private final double temperature;
    private final HttpClient httpClient;
    private final Gson gson;
    
    public AnthropicProvider() {
        this.apiKey = LLMConfig.getApiKey();
        this.baseUrl = LLMConfig.getApiBaseUrl();
        this.model = LLMConfig.getApiModel();
        this.temperature = LLMConfig.getApiTemperature();
        
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }
    
    @Override
    public String generateCode(String intent) throws LLMException {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new LLMException("API key not configured. Set SCRUM_API_KEY environment variable.");
        }
        
        try {
            String prompt = PromptTemplate.buildPrompt(intent);
            
            // Build Anthropic request format
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", prompt);
            
            JsonArray messages = new JsonArray();
            messages.add(message);
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);
            requestBody.add("messages", messages);
            requestBody.addProperty("max_tokens", 2000);
            requestBody.addProperty("temperature", temperature);
            
            String jsonBody = gson.toJson(requestBody);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/messages"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", ANTHROPIC_VERSION)
                    .timeout(Duration.ofSeconds(LLMConfig.getLLMTimeout()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                String errorBody = response.body();
                
                // Check for rate limiting (HTTP 429)
                if (response.statusCode() == 429) {
                    throw new RateLimitException("Anthropic rate limit exceeded: " + errorBody);
                }
                
                // Check for insufficient quota/credits (HTTP 402)
                if (response.statusCode() == 402) {
                    throw new QuotaExceededException("Anthropic insufficient credits or quota: " + errorBody);
                }
                
                // Check error message content for quota/rate limit indicators
                String lowerError = errorBody.toLowerCase();
                if (lowerError.contains("quota") || lowerError.contains("insufficient_quota") || 
                    lowerError.contains("billing") || lowerError.contains("credits")) {
                    throw new QuotaExceededException("Anthropic quota exceeded: " + errorBody);
                }
                if (lowerError.contains("rate_limit") || lowerError.contains("rate limit") || 
                    lowerError.contains("too many requests")) {
                    throw new RateLimitException("Anthropic rate limit exceeded: " + errorBody);
                }
                
                throw new LLMException("Anthropic API request failed with status " + response.statusCode() + ": " + errorBody);
            }
            
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            JsonArray content = jsonResponse.getAsJsonArray("content");
            String generatedCode = content.get(0).getAsJsonObject().get("text").getAsString();
            
            return cleanGeneratedCode(generatedCode);
            
        } catch (java.net.http.HttpTimeoutException e) {
            throw new LLMException(
                "Anthropic request timed out after " + LLMConfig.getLLMTimeout() + " seconds. " +
                "The model may be overloaded or the request too complex. " +
                "Try simplifying your intent or increasing SCRUM_LLM_TIMEOUT.", e);
        } catch (java.io.IOException e) {
            throw new LLMException(
                "Network error while connecting to Anthropic API: " + e.getMessage() + ". " +
                "Check your internet connection and API endpoint.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LLMException("Anthropic request was interrupted.", e);
        } catch (Exception e) {
            throw new LLMException("Failed to generate code via Anthropic API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Clean up markdown code blocks and extra formatting from generated code.
     */
    private String cleanGeneratedCode(String code) {
        // Remove markdown code blocks
        code = code.replaceAll("```(?:scrum)?\\s*", "");
        code = code.replaceAll("```\\s*$", "");
        
        // Trim whitespace
        code = code.trim();
        
        return code;
    }
    
    @Override
    public boolean isAvailable() {
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }
        
        try {
            // Test with a simple request to check if credentials are valid
            // We'll do a minimal request to avoid consuming tokens
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", "Hi");
            
            JsonArray messages = new JsonArray();
            messages.add(message);
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);
            requestBody.add("messages", messages);
            requestBody.addProperty("max_tokens", 10);
            
            String jsonBody = gson.toJson(requestBody);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/messages"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", ANTHROPIC_VERSION)
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getProviderName() {
        return "Anthropic Claude (" + model + ")";
    }
}
