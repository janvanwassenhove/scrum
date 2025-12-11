package scrum.expression.value;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents HTTP request data accessible within endpoint handlers.
 * Provides access to path parameters, query parameters, and request body.
 */
@Getter
public class HttpRequestValue extends Value<Map<String, Object>> {
    
    private final Map<String, String> pathParams;
    private final Map<String, String> queryParams;
    private final Object body;
    
    public HttpRequestValue(Map<String, String> pathParams, Map<String, String> queryParams, Object body) {
        super(buildValueMap(pathParams, queryParams, body));
        this.pathParams = pathParams != null ? pathParams : new HashMap<>();
        this.queryParams = queryParams != null ? queryParams : new HashMap<>();
        this.body = body;
    }
    
    private static Map<String, Object> buildValueMap(Map<String, String> pathParams, Map<String, String> queryParams, Object body) {
        Map<String, Object> value = new HashMap<>();
        value.put("pathParams", pathParams != null ? pathParams : new HashMap<>());
        value.put("queryParams", queryParams != null ? queryParams : new HashMap<>());
        value.put("body", body);
        return value;
    }
    
    /**
     * Get a path parameter value by name.
     */
    public String getPathParam(String name) {
        return pathParams.get(name);
    }
    
    /**
     * Get a query parameter value by name.
     */
    public String getQueryParam(String name) {
        return queryParams.get(name);
    }
    
    /**
     * Get the request body.
     */
    public Object getBody() {
        return body;
    }
}
