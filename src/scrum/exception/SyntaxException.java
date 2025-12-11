package scrum.exception;

import lombok.Getter;

/**
 * Syntax exception for Scrum-inspired error reporting.
 * Represents an IMPEDIMENT discovered during BACKLOG REFINEMENT.
 */
@Getter
public class SyntaxException extends ScrumLanguageException {
    private final String fileName;
    private final int line;
    private final int column;
    private final String snippet;
    private final String explanation;
    private final ImpedimentCode impedimentCode;
    
    // Legacy constructor for backward compatibility
    public SyntaxException(String message) {
        super(message);
        this.fileName = null;
        this.line = -1;
        this.column = -1;
        this.snippet = null;
        this.explanation = message;
        this.impedimentCode = ImpedimentCode.SCRUM_SYNTAX_UNKNOWN_001;
    }
    
    private SyntaxException(Builder builder) {
        super(builder.explanation);
        this.fileName = builder.fileName;
        this.line = builder.line;
        this.column = builder.column;
        this.snippet = builder.snippet;
        this.explanation = builder.explanation;
        this.impedimentCode = builder.impedimentCode;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String fileName;
        private int line = -1;
        private int column = -1;
        private String snippet;
        private String explanation;
        private ImpedimentCode impedimentCode = ImpedimentCode.SCRUM_SYNTAX_UNKNOWN_001;
        
        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public Builder line(int line) {
            this.line = line;
            return this;
        }
        
        public Builder column(int column) {
            this.column = column;
            return this;
        }
        
        public Builder snippet(String snippet) {
            this.snippet = snippet;
            return this;
        }
        
        public Builder explanation(String explanation) {
            this.explanation = explanation;
            return this;
        }
        
        public Builder impedimentCode(ImpedimentCode impedimentCode) {
            this.impedimentCode = impedimentCode;
            return this;
        }
        
        public SyntaxException build() {
            return new SyntaxException(this);
        }
    }
}
