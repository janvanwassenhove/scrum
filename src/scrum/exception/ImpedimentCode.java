package scrum.exception;

/**
 * Impediment codes for Scrum-style error reporting.
 * Each code represents a specific type of blocker encountered during
 * backlog refinement (syntax errors) or story execution (runtime errors).
 */
public enum ImpedimentCode {
    // Runtime impediments - execution blockers
    SCRUM_RUNTIME_ARITH_001("Arithmetic impediment (division by zero, overflow, etc.)"),
    SCRUM_RUNTIME_NAME_001("Missing or undefined value impediment"),
    SCRUM_RUNTIME_TYPE_001("Type mismatch impediment"),
    SCRUM_RUNTIME_ITERATION_001("Iteration impediment (non-iterable value)"),
    SCRUM_RUNTIME_PROPERTY_001("Property access impediment"),
    SCRUM_RUNTIME_UNKNOWN_001("Uncategorized runtime impediment"),
    
    // Syntax impediments - backlog refinement blockers
    SCRUM_SYNTAX_TOKEN_001("Unrecognized token or character"),
    SCRUM_SYNTAX_STRUCTURE_001("Misplaced or unfinished EPIC or USER STORY"),
    SCRUM_SYNTAX_ENDPOINT_001("Invalid API endpoint definition"),
    SCRUM_SYNTAX_EXPRESSION_001("Invalid expression or operator"),
    SCRUM_SYNTAX_UNEXPECTED_001("Unexpected token in context"),
    SCRUM_SYNTAX_UNKNOWN_001("Uncategorized syntax impediment");
    
    private final String description;
    
    ImpedimentCode(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return name().replace("_", "-");
    }
}
