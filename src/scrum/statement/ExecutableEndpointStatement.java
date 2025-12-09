package scrum.statement;

import lombok.Getter;
import scrum.context.definition.DefinitionScope;

/**
 * Represents an executable endpoint with a handler body.
 * This allows endpoints to contain actual logic that executes when the endpoint is called.
 */
@Getter
public class ExecutableEndpointStatement extends CompositeStatement {
    private final String name;
    private final String method;
    private final String path;
    private final String queryParams;
    private final String returnType;
    private final DefinitionScope definitionScope;

    public ExecutableEndpointStatement(String name, String method, String path, 
                                      String queryParams, String returnType, 
                                      DefinitionScope definitionScope) {
        this.name = name;
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.returnType = returnType;
        this.definitionScope = definitionScope;
    }

    @Override
    public void execute() {
        // Execute the endpoint handler logic (statements in the endpoint body)
        super.execute();
    }
}
