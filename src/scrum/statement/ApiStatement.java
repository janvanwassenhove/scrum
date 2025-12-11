package scrum.statement;

/**
 * Represents an API definition statement block.
 * Contains nested endpoint definitions and other statements.
 * When executed, registers the API in the ApiContext for runtime access.
 */
public class ApiStatement extends CompositeStatement {
    @Override
    public void execute() {
        // Execute child statements (endpoint registrations)
        super.execute();
        
        // API is already registered in DefinitionScope by the parser
        // Child endpoint statements are also executed during super.execute()
    }
}
