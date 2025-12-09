package scrum.exception;

import lombok.Getter;

/**
 * Runtime exception for Scrum-inspired error reporting.
 * Represents an IMPEDIMENT that blocked a USER STORY from completing.
 */
@Getter
public class ScrumRuntimeException extends ScrumLanguageException {
    private final String epicName;
    private final String storyName;
    private final String fileName;
    private final int line;
    private final int column;
    private final String snippet;
    private final ImpedimentCode impedimentCode;
    private final Throwable cause;
    
    private ScrumRuntimeException(Builder builder) {
        super(builder.message);
        this.epicName = builder.epicName;
        this.storyName = builder.storyName;
        this.fileName = builder.fileName;
        this.line = builder.line;
        this.column = builder.column;
        this.snippet = builder.snippet;
        this.impedimentCode = builder.impedimentCode;
        this.cause = builder.cause;
        if (this.cause != null) {
            initCause(this.cause);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String epicName;
        private String storyName;
        private String fileName;
        private int line = -1;
        private int column = -1;
        private String snippet;
        private ImpedimentCode impedimentCode = ImpedimentCode.SCRUM_RUNTIME_UNKNOWN_001;
        private String message;
        private Throwable cause;
        
        public Builder epicName(String epicName) {
            this.epicName = epicName;
            return this;
        }
        
        public Builder storyName(String storyName) {
            this.storyName = storyName;
            return this;
        }
        
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
        
        public Builder impedimentCode(ImpedimentCode impedimentCode) {
            this.impedimentCode = impedimentCode;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }
        
        public ScrumRuntimeException build() {
            return new ScrumRuntimeException(this);
        }
    }
}
