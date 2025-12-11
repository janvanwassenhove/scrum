package scrum.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents an individual endpoint definition within an API.
 * Stores endpoint metadata (method, path, query params, return type).
 * This is a declarative statement - endpoints are registered but not executed
 * until an HTTP request is received (future implementation phase).
 */
@RequiredArgsConstructor
@Getter
public class EndpointStatement implements Statement {
    private final String name;
    private final String method;
    private final String path;
    private final String queryParams;
    private final String returnType;

    @Override
    public void execute() {
        // Endpoint execution is declarative - metadata is stored in EndpointDefinition
        // The endpoint is registered as part of the API definition
        // Actual HTTP request handling would be implemented in a future phase
        // where an HTTP server would route requests to handler functions
        
        // For now, we silently register the endpoint (no output)
        // This allows API definitions to be parsed and stored without side effects
    }
}
