package scrum.context;

import scrum.context.definition.ApiDefinition;
import scrum.context.definition.DefinitionContext;
import scrum.expression.value.HttpRequestValue;
import scrum.expression.value.HttpResponseValue;
import scrum.expression.value.Value;
import scrum.statement.ExecutableEndpointStatement;
import scrum.statement.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Context for managing registered APIs and their endpoints at runtime.
 * Provides a registry for API definitions and methods to invoke endpoints.
 */
public class ApiContext {
    private static final Map<String, ApiDefinition> apiRegistry = new HashMap<>();
    private static final Map<String, Object> currentRequest = new HashMap<>();

    /**
     * Register an API definition
     */
    public static void registerApi(ApiDefinition apiDefinition) {
        apiRegistry.put(apiDefinition.getName(), apiDefinition);
    }

    /**
     * Get a registered API by name
     */
    public static ApiDefinition getApi(String name) {
        return apiRegistry.get(name);
    }

    /**
     * Get current request context
     */
    public static Map<String, Object> getCurrentRequest() {
        return new HashMap<>(currentRequest);
    }

    /**
     * Set request context for endpoint execution
     */
    public static void setRequestContext(Map<String, Object> requestData) {
        currentRequest.clear();
        currentRequest.putAll(requestData);
    }

    /**
     * Clear request context
     */
    public static void clearRequestContext() {
        currentRequest.clear();
    }

    /**
     * Clear all registered APIs (useful for testing)
     */
    public static void clearRegistry() {
        apiRegistry.clear();
        currentRequest.clear();
    }

    /**
     * Invoke an endpoint by matching path and extracting parameters
     * 
     * @param apiName The name of the API
     * @param requestPath The incoming request path
     * @param queryParams Query parameters map
     * @param body Request body
     * @return The response value from the endpoint
     */
    public static Value<?> invokeEndpoint(String apiName, String requestPath, 
                                         Map<String, String> queryParams, Object body) {
        ApiDefinition api = apiRegistry.get(apiName);
        if (api == null) {
            throw new RuntimeException("API not found: " + apiName);
        }

        // Find matching endpoint
        for (Statement statement : api.getStatement().getStatements2Execute()) {
            if (statement instanceof ExecutableEndpointStatement) {
                ExecutableEndpointStatement endpoint = (ExecutableEndpointStatement) statement;
                
                // Extract path parameters
                Map<String, String> pathParams = extractPathParams(endpoint.getPath(), requestPath);
                if (pathParams != null) {
                    // Set up request context
                    HttpRequestValue requestValue = new HttpRequestValue(pathParams, queryParams, body);
                    Map<String, Object> requestData = new HashMap<>();
                    requestData.put("request", requestValue);
                    requestData.put("pathParams", pathParams);
                    requestData.put("queryParams", queryParams);
                    requestData.put("body", body);
                    setRequestContext(requestData);
                    
                    // Execute endpoint in its own scope
                    DefinitionContext.pushScope(endpoint.getDefinitionScope());
                    MemoryContext.pushScope(MemoryContext.newScope());
                    try {
                        endpoint.execute();
                        
                        // For now, return a success response
                        // A more sophisticated implementation would capture RESPOND WITH statements
                        return new HttpResponseValue("Success", 200);
                    } finally {
                        MemoryContext.endScope();
                        DefinitionContext.endScope();
                        clearRequestContext();
                    }
                }
            }
        }
        
        throw new RuntimeException("No matching endpoint found for path: " + requestPath);
    }

    /**
     * Extract path parameters from a request path matching a pattern
     * 
     * @param pattern The endpoint path pattern (e.g., "/users/{id}")
     * @param requestPath The actual request path (e.g., "/users/123")
     * @return Map of parameter names to values, or null if path doesn't match
     */
    private static Map<String, String> extractPathParams(String pattern, String requestPath) {
        Map<String, String> params = new HashMap<>();
        
        // Convert path pattern to regex (replace {param} with capture groups)
        String regexPattern = pattern.replaceAll("\\{([^}]+)\\}", "([^/]+)");
        Pattern p = Pattern.compile("^" + regexPattern + "$");
        Matcher matcher = p.matcher(requestPath);
        
        if (!matcher.matches()) {
            return null; // Path doesn't match pattern
        }
        
        // Extract parameter names from pattern
        Pattern paramPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher paramMatcher = paramPattern.matcher(pattern);
        
        int groupIndex = 1;
        while (paramMatcher.find()) {
            String paramName = paramMatcher.group(1);
            String paramValue = matcher.group(groupIndex++);
            params.put(paramName, paramValue);
        }
        
        return params;
    }
}
