package scrum.context.definition;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.statement.ApiStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an API definition with its base path and nested endpoints.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApiDefinition implements Definition {
    @EqualsAndHashCode.Include
    private final String name;
    private final String basePath;
    private final ApiStatement statement;
    private final DefinitionScope definitionScope;
    private final List<EndpointDefinition> endpoints;

    public ApiDefinition(String name, String basePath, ApiStatement statement, DefinitionScope definitionScope) {
        this.name = name;
        this.basePath = basePath;
        this.statement = statement;
        this.definitionScope = definitionScope;
        this.endpoints = new ArrayList<>();
    }

    public void addEndpoint(EndpointDefinition endpoint) {
        this.endpoints.add(endpoint);
    }
}
