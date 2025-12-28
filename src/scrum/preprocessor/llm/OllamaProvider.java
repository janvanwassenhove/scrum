package scrum.preprocessor.llm;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * LLM provider implementation for Ollama.
 * Communicates with a local Ollama instance via HTTP API.
 */
public class OllamaProvider implements LLMProvider {
    
    private final String baseUrl;
    private final String model;
    private final double temperature;
    private final HttpClient httpClient;
    private final Gson gson;
    
    public OllamaProvider() {
        this.baseUrl = LLMConfig.getOllamaUrl();
        this.model = LLMConfig.getOllamaModel();
        this.temperature = LLMConfig.getOllamaTemperature();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }
    
    @Override
    public String generateCode(String intent) throws LLMException {
        String prompt = PromptTemplate.buildPrompt(intent);
        
        // Build request JSON
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", model);
        requestJson.addProperty("prompt", prompt);
        requestJson.addProperty("stream", false);
        requestJson.addProperty("temperature", temperature);
        
        // Send request to Ollama
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/generate"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(LLMConfig.getLLMTimeout()))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestJson)))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                String errorBody = response.body();
                
                // Check for rate limiting (HTTP 429) - rare for Ollama but possible with proxies
                if (response.statusCode() == 429) {
                    throw new RateLimitException("Ollama rate limit exceeded: " + errorBody);
                }
                
                throw new LLMException("Ollama API returned status " + response.statusCode() + ": " + errorBody);
            }
            
            // Parse response
            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            String generatedCode = responseJson.get("response").getAsString().trim();
            
            // Clean up the response - remove markdown code blocks if present
            generatedCode = cleanGeneratedCode(generatedCode);
            
            return generatedCode;
            
        } catch (java.net.http.HttpTimeoutException e) {
            throw new LLMException(
                "Ollama request timed out after " + LLMConfig.getLLMTimeout() + " seconds. " +
                "The model may be slow or the request too complex. " +
                "Try increasing SCRUM_LLM_TIMEOUT.", e);
        } catch (java.net.ConnectException e) {
            throw new LLMException(
                "Cannot connect to Ollama at " + baseUrl + ". " +
                "Make sure Ollama is running with: ollama serve", e);
        } catch (IOException | InterruptedException e) {
            throw new LLMException("Failed to communicate with Ollama: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new LLMException("Failed to parse Ollama response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Clean generated code by removing markdown formatting and extra whitespace.
     */
    private String cleanGeneratedCode(String code) {
        // Remove markdown code blocks
        code = code.replaceAll("```scrum\\n?", "");
        code = code.replaceAll("```SCRUM\\n?", "");
        code = code.replaceAll("```\\n?", "");
        
        // Remove leading/trailing whitespace
        code = code.trim();
        
        return code;
    }
    
    @Override
    public boolean isAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/tags"))
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
        return "ollama";
    }
}
