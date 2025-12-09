package scrum.expression.value;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents HTTP response data from an endpoint handler.
 * Stores response body and status information.
 */
@Getter
public class HttpResponseValue extends Value<Map<String, Object>> {
    
    private final Object responseBody;
    private final int statusCode;
    
    public HttpResponseValue(Object responseBody, int statusCode) {
        super(buildValueMap(responseBody, statusCode));
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }
    
    public HttpResponseValue(Object responseBody) {
        this(responseBody, 200);
    }
    
    private static Map<String, Object> buildValueMap(Object responseBody, int statusCode) {
        Map<String, Object> value = new HashMap<>();
        value.put("body", responseBody);
        value.put("statusCode", statusCode);
        return value;
    }
}
