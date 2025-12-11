package scrum.intellij;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Syntax highlighter for SCRUM language.
 * Defines colors for different token types.
 */
public class ScrumSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD = 
        createTextAttributesKey("SCRUM_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    
    public static final TextAttributesKey API_KEYWORD = 
        createTextAttributesKey("SCRUM_API_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    
    public static final TextAttributesKey DIRECTIVE = 
        createTextAttributesKey("SCRUM_DIRECTIVE", DefaultLanguageHighlighterColors.METADATA);
    
    public static final TextAttributesKey STRING = 
        createTextAttributesKey("SCRUM_STRING", DefaultLanguageHighlighterColors.STRING);
    
    public static final TextAttributesKey NUMBER = 
        createTextAttributesKey("SCRUM_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    
    public static final TextAttributesKey LINE_COMMENT = 
        createTextAttributesKey("SCRUM_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    
    public static final TextAttributesKey BLOCK_COMMENT = 
        createTextAttributesKey("SCRUM_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    
    public static final TextAttributesKey OPERATOR = 
        createTextAttributesKey("SCRUM_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    
    public static final TextAttributesKey IDENTIFIER = 
        createTextAttributesKey("SCRUM_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ScrumLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(ScrumTokenTypes.KEYWORD)) {
            return pack(KEYWORD);
        } else if (tokenType.equals(ScrumTokenTypes.API_KEYWORD)) {
            return pack(API_KEYWORD);
        } else if (tokenType.equals(ScrumTokenTypes.DIRECTIVE)) {
            return pack(DIRECTIVE);
        } else if (tokenType.equals(ScrumTokenTypes.STRING)) {
            return pack(STRING);
        } else if (tokenType.equals(ScrumTokenTypes.NUMBER)) {
            return pack(NUMBER);
        } else if (tokenType.equals(ScrumTokenTypes.LINE_COMMENT)) {
            return pack(LINE_COMMENT);
        } else if (tokenType.equals(ScrumTokenTypes.BLOCK_COMMENT)) {
            return pack(BLOCK_COMMENT);
        } else if (tokenType.equals(ScrumTokenTypes.OPERATOR)) {
            return pack(OPERATOR);
        } else if (tokenType.equals(ScrumTokenTypes.IDENTIFIER)) {
            return pack(IDENTIFIER);
        }
        return TextAttributesKey.EMPTY_ARRAY;
    }
}
