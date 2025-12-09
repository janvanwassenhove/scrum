package scrum.exception;

/**
 * Formats Scrum-inspired impediment messages for user-friendly error reporting.
 * Transforms technical exceptions into narrative-style blockers using Scrum terminology.
 */
public class ImpedimentFormatter {
    
    /**
     * Format a runtime impediment (story execution blocker).
     */
    public static String formatRuntimeImpediment(ScrumRuntimeException ex) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║  SCRUM IMPEDIMENT - USER STORY COULD NOT BE COMPLETED              ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        // EPIC and USER STORY context
        if (ex.getEpicName() != null) {
            sb.append(String.format("EPIC       \"%s\"\n", ex.getEpicName()));
        }
        if (ex.getStoryName() != null) {
            sb.append(String.format("USER STORY \"%s\"\n", ex.getStoryName()));
        }
        
        // File location
        String fileName = ex.getFileName() != null ? ex.getFileName() : "<unknown>";
        String location = String.format("FILE %s", fileName);
        if (ex.getLine() > 0) {
            location += String.format(", LINE %d", ex.getLine());
        }
        if (ex.getColumn() > 0) {
            location += String.format(", COLUMN %d", ex.getColumn());
        }
        sb.append(location).append("\n\n");
        
        // What was being attempted
        sb.append("DURING EXECUTION OF THIS STORY I TRIED TO:\n");
        String snippet = ex.getSnippet() != null ? ex.getSnippet() : "<code not available>";
        sb.append("    ").append(snippet).append("\n\n");
        
        // The blocker
        sb.append("BUT I ENCOUNTERED A BLOCKER:\n");
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected impediment occurred.";
        sb.append("    ").append(message).append("\n\n");
        
        // Impediment code
        sb.append(String.format("IMPEDIMENT CODE: %s\n", ex.getImpedimentCode()));
        
        // Root cause (if available)
        if (ex.getCause() != null) {
            sb.append(String.format("ROOT CAUSE: %s: %s\n", 
                ex.getCause().getClass().getSimpleName(), 
                ex.getCause().getMessage()));
        }
        
        sb.append("\n");
        return sb.toString();
    }
    
    /**
     * Format a syntax impediment (backlog refinement blocker).
     */
    public static String formatSyntaxImpediment(SyntaxException ex) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║  SCRUM IMPEDIMENT – BACKLOG ITEM NOT READY                     ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        // File location
        String fileName = ex.getFileName() != null ? ex.getFileName() : "<unknown>";
        String location = String.format("FILE %s", fileName);
        if (ex.getLine() > 0) {
            location += String.format(", LINE %d", ex.getLine());
        }
        if (ex.getColumn() > 0) {
            location += String.format(", COLUMN %d", ex.getColumn());
        }
        sb.append(location).append("\n\n");
        
        // Problematic code
        sb.append("DURING BACKLOG REFINEMENT I COULD NOT UNDERSTAND THIS PART:\n");
        String snippet = ex.getSnippet() != null ? ex.getSnippet() : "<code not available>";
        sb.append("    ").append(snippet).append("\n\n");
        
        // Explanation
        sb.append("BECAUSE:\n");
        String explanation = ex.getExplanation() != null ? ex.getExplanation() : "Syntax error.";
        sb.append("    ").append(explanation).append("\n\n");
        
        // Impediment code
        sb.append(String.format("IMPEDIMENT CODE: %s\n", ex.getImpedimentCode()));
        
        sb.append("\n");
        return sb.toString();
    }
    
    /**
     * Format a token impediment (lexical analysis blocker).
     */
    public static String formatTokenImpediment(TokenException ex) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║  SCRUM IMPEDIMENT – BACKLOG ITEM NOT READY                     ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        // File location
        String fileName = ex.getFileName() != null ? ex.getFileName() : "<unknown>";
        String location = String.format("FILE %s", fileName);
        if (ex.getLine() > 0) {
            location += String.format(", LINE %d", ex.getLine());
        }
        if (ex.getColumn() > 0) {
            location += String.format(", COLUMN %d", ex.getColumn());
        }
        sb.append(location).append("\n\n");
        
        // Problematic code
        sb.append("DURING BACKLOG REFINEMENT I COULD NOT UNDERSTAND THIS PART:\n");
        String snippet = ex.getSnippet() != null ? ex.getSnippet() : "<code not available>";
        sb.append("    ").append(snippet).append("\n\n");
        
        // Explanation
        sb.append("BECAUSE:\n");
        String explanation = ex.getExplanation() != null ? ex.getExplanation() : "Unrecognized token.";
        sb.append("    ").append(explanation).append("\n\n");
        
        // Impediment code
        sb.append(String.format("IMPEDIMENT CODE: %s\n", ex.getImpedimentCode()));
        
        sb.append("\n");
        return sb.toString();
    }
}
