package scrum.context.definition;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Represents an endpoint definition within an API.
 * Contains metadata about HTTP method, path, query parameters, and return type.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EndpointDefinition {
    @EqualsAndHashCode.Include
    private final String name;
    private final String method;
    private final String path;
    private final List<String> queryParams;
    private final String returnType;
}
