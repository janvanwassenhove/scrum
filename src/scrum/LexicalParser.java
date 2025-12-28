package scrum;

import scrum.exception.TokenException;
import scrum.token.Token;
import scrum.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LexicalParser {
    private final List<Token> tokens;
    private final String source;
    private final List<Integer> linesIndices;

    public LexicalParser(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.linesIndices = IntStream
                .iterate(source.indexOf("\n"), index -> index >= 0, index -> source.indexOf("\n", index + 1))
                .boxed()
                .collect(Collectors.toList());
    }

    public List<Token> parse() {
        int position = 0;
        while (position < source.length()) {
            position += nextToken(position);
        }
        return tokens;
    }

    private int nextToken(int position) {
        String nextToken = source.substring(position);

        int row = IntStream.range(0, linesIndices.size())
                .filter(i -> position <= linesIndices.get(i))
                .findFirst()
                .orElse(linesIndices.size()) + 1;

        // Special handling for #INTENT blocks - capture everything until #END INTENT
        if (nextToken.startsWith("#INTENT")) {
            // Find the #INTENT keyword
            Pattern intentStartPattern = Pattern.compile("^#INTENT");
            Matcher intentMatcher = intentStartPattern.matcher(nextToken);
            if (intentMatcher.find()) {
                // Add #INTENT keyword token
                Token intentToken = Token.builder()
                        .type(TokenType.Keyword)
                        .value("#INTENT")
                        .row(row)
                        .build();
                tokens.add(intentToken);
                
                int intentLength = intentMatcher.group().length();
                
                // Find #END INTENT
                int endIntentPos = source.indexOf("#END INTENT", position + intentLength);
                if (endIntentPos == -1) {
                    throw new TokenException(String.format("Missing #END INTENT at line %d", row));
                }
                
                // Extract the raw text between #INTENT and #END INTENT
                String intentText = source.substring(position + intentLength, endIntentPos);
                
                // Add the intent text as a single Text token (preserve whitespace and newlines)
                if (!intentText.trim().isEmpty()) {
                    Token textToken = Token.builder()
                            .type(TokenType.Text)
                            .value(intentText)
                            .row(row)
                            .build();
                    tokens.add(textToken);
                }
                
                // Add #END INTENT keyword token
                int endRow = row + countNewlines(intentText);
                Token endIntentToken = Token.builder()
                        .type(TokenType.Keyword)
                        .value("#END INTENT")
                        .row(endRow)
                        .build();
                tokens.add(endIntentToken);
                
                // Return total length consumed
                return (endIntentPos - position) + "#END INTENT".length();
            }
        }

        for (TokenType tokenType : TokenType.values()) {
            Pattern pattern = Pattern.compile("^" + tokenType.getRegex());
            Matcher matcher = pattern.matcher(nextToken);
            if (matcher.find()) {
                if (tokenType != TokenType.Whitespace) {
                    // group(1) is used to get text literal without double quotes
                    String value = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
                    Token token = Token.builder().type(tokenType).value(value).row(row).build();
                    tokens.add(token);
                }
                return matcher.group().length();
            }
        }

        throw new TokenException(String.format("invalid expression at line %d", row));
    }

    private int countNewlines(String text) {
        return (int) text.chars().filter(ch -> ch == '\n').count();
    }

}
