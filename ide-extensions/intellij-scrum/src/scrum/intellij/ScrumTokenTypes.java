package scrum.intellij;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Token types for SCRUM language lexical analysis.
 */
public interface ScrumTokenTypes {
    IElementType KEYWORD = new IElementType("KEYWORD", ScrumLanguage.INSTANCE);
    IElementType API_KEYWORD = new IElementType("API_KEYWORD", ScrumLanguage.INSTANCE);
    IElementType DIRECTIVE = new IElementType("DIRECTIVE", ScrumLanguage.INSTANCE);
    IElementType IDENTIFIER = new IElementType("IDENTIFIER", ScrumLanguage.INSTANCE);
    IElementType STRING = new IElementType("STRING", ScrumLanguage.INSTANCE);
    IElementType NUMBER = new IElementType("NUMBER", ScrumLanguage.INSTANCE);
    IElementType LINE_COMMENT = new IElementType("LINE_COMMENT", ScrumLanguage.INSTANCE);
    IElementType BLOCK_COMMENT = new IElementType("BLOCK_COMMENT", ScrumLanguage.INSTANCE);
    IElementType OPERATOR = new IElementType("OPERATOR", ScrumLanguage.INSTANCE);

    TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT);
    TokenSet STRINGS = TokenSet.create(STRING);
    TokenSet KEYWORDS = TokenSet.create(KEYWORD, API_KEYWORD, DIRECTIVE);
}
