package scrum.statement;

import lombok.Getter;

/**
 * Represents an #INTENT block within a USER STORY.
 * Stores the raw natural language intent that will be translated to SCRUM code
 * by the preprocessor before execution.
 */
@Getter
public class IntentBlockStatement extends CompositeStatement {
    
    private final String rawIntent;
    private boolean generated;
    
    public IntentBlockStatement(String rawIntent) {
        super();
        this.rawIntent = rawIntent;
        this.generated = false;
    }
    
    /**
     * Mark this intent block as having been processed and code generated.
     */
    public void setGenerated(boolean generated) {
        this.generated = generated;
    }
    
    /**
     * Check if this intent block has been processed.
     */
    public boolean isGenerated() {
        return generated;
    }
    
    @Override
    public void execute() {
        if (!generated) {
            throw new RuntimeException(
                "Intent block was not processed by preprocessor. " +
                "This indicates a bug in the compilation pipeline. Intent: " + 
                rawIntent.substring(0, Math.min(50, rawIntent.length())) + "..."
            );
        }
        
        // Execute the generated child statements
        super.execute();
    }
}
