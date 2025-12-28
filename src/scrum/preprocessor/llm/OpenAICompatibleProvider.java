package scrum.preprocessor.llm;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Generic OpenAI-compatible API provider.
 * Works with OpenAI, Groq, Cerebras, Together.ai, and other OpenAI-compatible endpoints.
 * 
 * Configuration via environment variables:
 * - SCRUM_API_KEY: API key for authentication
 * - SCRUM_API_BASE_URL: Base URL (default: https://api.openai.com/v1)
 * - SCRUM_API_MODEL: Model name (default: gpt-4o-mini)
 * - SCRUM_API_TEMPERATURE: Temperature (default: 0.3)
 * 
 * Examples:
 * OpenAI:     SCRUM_API_BASE_URL=https://api.openai.com/v1 SCRUM_API_MODEL=gpt-4o-mini
 *             Also supports: gpt-4o, o1, o3-mini
 * Groq:       SCRUM_API_BASE_URL=https://api.groq.com/openai/v1 SCRUM_API_MODEL=llama-3.3-70b-versatile
 * Cerebras:   SCRUM_API_BASE_URL=https://api.cerebras.ai/v1 SCRUM_API_MODEL=llama3.1-8b
 * Together:   SCRUM_API_BASE_URL=https://api.together.xyz/v1 SCRUM_API_MODEL=meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo
 */
public class OpenAICompatibleProvider implements LLMProvider {
    
    private final String apiKey;
    private final String baseUrl;
    private final String model;
    private final double temperature;
    private final HttpClient httpClient;
    private final Gson gson;
    
    public OpenAICompatibleProvider() {
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
            
            // Build OpenAI-compatible request
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", prompt);
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);
            requestBody.add("messages", gson.toJsonTree(new JsonObject[]{gson.fromJson(message, JsonObject.class)}));
            requestBody.addProperty("temperature", temperature);
            requestBody.addProperty("max_tokens", 2000);
            
            String jsonBody = gson.toJson(requestBody);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(LLMConfig.getLLMTimeout()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                String errorBody = response.body();
                
                // Check for rate limiting (HTTP 429)
                if (response.statusCode() == 429) {
                    throw new RateLimitException("Rate limit exceeded: " + errorBody);
                }
                
                // Check for insufficient quota/credits (HTTP 402)
                if (response.statusCode() == 402) {
                    throw new QuotaExceededException("Insufficient credits or quota: " + errorBody);
                }
                
                // Check error message content for quota/rate limit indicators
                String lowerError = errorBody.toLowerCase();
                if (lowerError.contains("quota") || lowerError.contains("insufficient_quota") || 
                    lowerError.contains("billing") || lowerError.contains("credits")) {
                    throw new QuotaExceededException("Quota exceeded: " + errorBody);
                }
                if (lowerError.contains("rate_limit") || lowerError.contains("rate limit") || 
                    lowerError.contains("too many requests")) {
                    throw new RateLimitException("Rate limit exceeded: " + errorBody);
                }
                
                throw new LLMException("API request failed with status " + response.statusCode() + ": " + errorBody);
            }
            
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            String generatedCode = jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
            
            return cleanGeneratedCode(generatedCode);
            
        } catch (java.net.http.HttpTimeoutException e) {
            throw new LLMException(
                "LLM request timed out after " + LLMConfig.getLLMTimeout() + " seconds. " +
                "The model may be overloaded or the request too complex. " +
                "Try simplifying your intent or increasing SCRUM_LLM_TIMEOUT.", e);
        } catch (java.io.IOException e) {
            throw new LLMException(
                "Network error while connecting to LLM API: " + e.getMessage() + ". " +
                "Check your internet connection and API endpoint.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LLMException("LLM request was interrupted.", e);
        } catch (Exception e) {
            throw new LLMException("Failed to generate code via API: " + e.getMessage(), e);
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
            // Test with a simple request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/models"))
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getProviderName() {
        // Try to determine provider from URL
        if (baseUrl.contains("groq")) {
            return "Groq";
        } else if (baseUrl.contains("cerebras")) {
            return "Cerebras";
        } else if (baseUrl.contains("together")) {
            return "Together.ai";
        } else if (baseUrl.contains("openai")) {
            return "OpenAI";
        } else {
            return "OpenAI-compatible API";
        }
    }
}
