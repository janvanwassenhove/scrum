package scrum.intellij;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Lexical analyzer for SCRUM language.
 * Breaks SCRUM code into tokens for syntax highlighting.
 */
public class ScrumLexer extends LexerBase {
    private CharSequence buffer;
    private int startOffset;
    private int endOffset;
    private int currentOffset;
    private IElementType currentTokenType;
    private int currentTokenEnd;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.currentOffset = startOffset;
        advance();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        return currentTokenType;
    }

    @Override
    public int getTokenStart() {
        return currentOffset;
    }

    @Override
    public int getTokenEnd() {
        return currentTokenEnd;
    }

    @Override
    public void advance() {
        if (currentOffset >= endOffset) {
            currentTokenType = null;
            return;
        }

        currentOffset = currentTokenEnd;
        if (currentOffset >= endOffset) {
            currentTokenType = null;
            return;
        }

        // Skip whitespace
        while (currentOffset < endOffset && Character.isWhitespace(buffer.charAt(currentOffset))) {
            currentOffset++;
        }

        if (currentOffset >= endOffset) {
            currentTokenType = null;
            return;
        }

        char c = buffer.charAt(currentOffset);

        // Comments
        if (c == '-' && currentOffset + 1 < endOffset && buffer.charAt(currentOffset + 1) == '-') {
            if (currentOffset + 3 < endOffset && 
                buffer.charAt(currentOffset + 2) == '[' && 
                buffer.charAt(currentOffset + 3) == '[') {
                // Block comment
                currentTokenEnd = findBlockCommentEnd(currentOffset + 4);
                currentTokenType = ScrumTokenTypes.BLOCK_COMMENT;
            } else {
                // Line comment
                currentTokenEnd = findLineEnd(currentOffset);
                currentTokenType = ScrumTokenTypes.LINE_COMMENT;
            }
            return;
        }

        // Strings
        if (c == '"') {
            currentTokenEnd = findStringEnd(currentOffset + 1);
            currentTokenType = ScrumTokenTypes.STRING;
            return;
        }

        // Numbers
        if (Character.isDigit(c)) {
            currentTokenEnd = findNumberEnd(currentOffset);
            currentTokenType = ScrumTokenTypes.NUMBER;
            return;
        }

        // Keywords and identifiers
        if (Character.isLetter(c) || c == '#') {
            currentTokenEnd = findIdentifierEnd(currentOffset);
            String text = buffer.subSequence(currentOffset, currentTokenEnd).toString();
            currentTokenType = getKeywordType(text);
            return;
        }

        // Operators
        if ("(){}[],;:=<>!+-*/".indexOf(c) >= 0) {
            currentTokenEnd = currentOffset + 1;
            currentTokenType = ScrumTokenTypes.OPERATOR;
            return;
        }

        // Default: single character
        currentTokenEnd = currentOffset + 1;
        currentTokenType = ScrumTokenTypes.IDENTIFIER;
    }

    private int findLineEnd(int start) {
        int pos = start;
        while (pos < endOffset && buffer.charAt(pos) != '\n') {
            pos++;
        }
        return pos;
    }

    private int findBlockCommentEnd(int start) {
        int pos = start;
        while (pos + 1 < endOffset) {
            if (buffer.charAt(pos) == ']' && buffer.charAt(pos + 1) == ']') {
                return pos + 2;
            }
            pos++;
        }
        return endOffset;
    }

    private int findStringEnd(int start) {
        int pos = start;
        while (pos < endOffset) {
            char c = buffer.charAt(pos);
            if (c == '"') {
                return pos + 1;
            }
            if (c == '\\' && pos + 1 < endOffset) {
                pos++; // Skip escaped character
            }
            pos++;
        }
        return endOffset;
    }

    private int findNumberEnd(int start) {
        int pos = start;
        while (pos < endOffset && (Character.isDigit(buffer.charAt(pos)) || buffer.charAt(pos) == '.')) {
            pos++;
        }
        return pos;
    }

    private int findIdentifierEnd(int start) {
        int pos = start;
        while (pos < endOffset) {
            char c = buffer.charAt(pos);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != ' ') {
                break;
            }
            pos++;
        }
        return pos;
    }

    private IElementType getKeywordType(String text) {
        String upper = text.toUpperCase().trim();
        
        // Directives
        if (upper.startsWith("#")) {
            return ScrumTokenTypes.DIRECTIVE;
        }
        
        // Control keywords
        if (upper.equals("SPRINT") || upper.equals("ENDSPRINT") || 
            upper.equals("STORY") || upper.equals("TASK") || 
            upper.equals("DELIVER") || upper.equals("IF") || 
            upper.equals("THEN") || upper.equals("ELSE") || 
            upper.equals("ENDIF") || upper.equals("WHILE") || 
            upper.equals("ENDWHILE") || upper.equals("FOR") || 
            upper.equals("ENDFOR")) {
            return ScrumTokenTypes.KEYWORD;
        }
        
        // API keywords
        if (upper.contains("I WANT TO DEFINE") || upper.contains("END OF") ||
            upper.contains("BASE IS") || upper.contains("METHOD IS") ||
            upper.contains("PATH IS") || upper.contains("RETURNS IS")) {
            return ScrumTokenTypes.API_KEYWORD;
        }
        
        return ScrumTokenTypes.IDENTIFIER;
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}
