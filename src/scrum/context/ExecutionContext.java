package scrum.context;

import lombok.Builder;
import lombok.Getter;

/**
 * Thread-local execution context for tracking the current execution state.
 * This enables Scrum-inspired error messages with EPIC, USER STORY, and file context.
 */
public class ExecutionContext {
    
    @Builder
    @Getter
    public static class Context {
        private final String fileName;
        private final String sourceCode;
        private String epicName;
        private String storyName;
        
        public void setEpicName(String epicName) {
            this.epicName = epicName;
        }
        
        public void setStoryName(String storyName) {
            this.storyName = storyName;
        }
    }
    
    private static final ThreadLocal<Context> context = new ThreadLocal<>();
    
    /**
     * Initialize the execution context with file and source information.
     */
    public static void initialize(String fileName, String sourceCode) {
        context.set(Context.builder()
                .fileName(fileName)
                .sourceCode(sourceCode)
                .build());
    }
    
    /**
     * Get the current execution context.
     */
    public static Context get() {
        return context.get();
    }
    
    /**
     * Set the current EPIC (class) being executed.
     */
    public static void setEpicName(String epicName) {
        Context ctx = context.get();
        if (ctx != null) {
            ctx.setEpicName(epicName);
        }
    }
    
    /**
     * Set the current USER STORY (function) being executed.
     */
    public static void setStoryName(String storyName) {
        Context ctx = context.get();
        if (ctx != null) {
            ctx.setStoryName(storyName);
        }
    }
    
    /**
     * Clear the execution context.
     */
    public static void clear() {
        context.remove();
    }
    
    /**
     * Extract a source code snippet for the given line number.
     */
    public static String getSourceSnippet(int line) {
        Context ctx = context.get();
        if (ctx == null || ctx.getSourceCode() == null) {
            return "<source not available>";
        }
        
        String[] lines = ctx.getSourceCode().split("\n");
        if (line < 1 || line > lines.length) {
            return "<line out of range>";
        }
        
        return lines[line - 1].trim();
    }
}
