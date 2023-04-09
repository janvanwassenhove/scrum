package scrum;

import scrum.token.Token;
import scrum.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexicalParserTest {

    @Test
    public void testPrint() {
        String source = "SAY \"Hello World\"";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertEquals(2, tokens.size());

        int count = 0;
        assertEquals(TokenType.Keyword, tokens.get(count).getType());
        assertEquals("SAY", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals("Hello World", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());
    }

    @Test
    public void testInput() {

        String source = "ASK a";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertEquals(2, tokens.size());

        int count = 0;
        assertEquals(TokenType.Keyword, tokens.get(count).getType());
        assertEquals("ASK", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("a", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());
    }

    @Test
    public void testAssignment() {

        String source = "a = 2 + 5";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertEquals(5, tokens.size());

        int count = 0;
        assertEquals(TokenType.Variable, tokens.get(count).getType());
        assertEquals("a", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("=", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Numeric, tokens.get(++count).getType());
        assertEquals("2", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("+", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Numeric, tokens.get(++count).getType());
        assertEquals("5", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());
    }

    @Test
    public void testCondition() {

        String source = "IF a > 5\n" +
                        "    SAY \"a greater than 5\"\n" +
                        "ELSEIF a >= 1\n" +
                        "    SAY \"a greater than or equal to 1\"\n" +
                        "ELSE\n" +
                        "    SAY \"a less than 1\"\n" +
                        "END IF";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertEquals(22, tokens.size());

        int count = 0;
        assertEquals(TokenType.Keyword, tokens.get(count).getType());
        assertEquals("IF", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("a", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals(">", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Numeric, tokens.get(++count).getType());
        assertEquals("5", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("SAY", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals("a greater than 5", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("ELSEIF", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("a", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals(">=", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Numeric, tokens.get(++count).getType());
        assertEquals("1", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("SAY", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals("a greater than or equal to 1", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("ELSE", tokens.get(count).getValue());
        assertEquals(5, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(5, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("SAY", tokens.get(count).getValue());
        assertEquals(6, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals("a less than 1", tokens.get(count).getValue());
        assertEquals(6, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(6, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("END IF", tokens.get(count).getValue());
        assertEquals(7, tokens.get(count).getRow());
    }

    @Test
    public void testClass() {

        String source = "EPIC \"Person\" USING [ name, age ]\n" +
                        "END OF EPIC\n" +
                        "person IS NEW Person USING [\"mITy John\", 45]\n" +
                        "SAY person :: name + \" IS \" + person :: age + \" years old\"";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertEquals(32, tokens.size());

        int count = 0;
        assertEquals(TokenType.Keyword, tokens.get(count).getType());
        assertEquals("EPIC", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals("Person", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.GroupDivider, tokens.get(++count).getType());
        assertEquals("USING [", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("name", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.GroupDivider, tokens.get(++count).getType());
        assertEquals(",", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("age", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.GroupDivider, tokens.get(++count).getType());
        assertEquals("]", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("END OF EPIC", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("person", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("IS", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("NEW", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("Person", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.GroupDivider, tokens.get(++count).getType());
        assertEquals("USING [", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals("mITy John", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.GroupDivider, tokens.get(++count).getType());
        assertEquals(",", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Numeric, tokens.get(++count).getType());
        assertEquals("45", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.GroupDivider, tokens.get(++count).getType());
        assertEquals("]", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(3, tokens.get(count).getRow());

        assertEquals(TokenType.Keyword, tokens.get(++count).getType());
        assertEquals("SAY", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("person", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("::", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("name", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("+", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals(" IS ", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("+", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("person", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("::", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("age", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("+", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());

        assertEquals(TokenType.Text, tokens.get(++count).getType());
        assertEquals(" years old", tokens.get(count).getValue());
        assertEquals(4, tokens.get(count).getRow());
    }

    @Test
    public void testComment() {
        String source = "#REVIEW a equals 5\n" +
                        "a IS 5 #REVIEW a equal to 5";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertEquals(6, tokens.size());

        int count = 0;
        assertEquals(TokenType.Comment, tokens.get(count).getType());
        assertEquals("#REVIEW a equals 5", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.LineBreak, tokens.get(++count).getType());
        assertEquals("\n", tokens.get(count).getValue());
        assertEquals(1, tokens.get(count).getRow());

        assertEquals(TokenType.Variable, tokens.get(++count).getType());
        assertEquals("a", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.Operator, tokens.get(++count).getType());
        assertEquals("IS", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.Numeric, tokens.get(++count).getType());
        assertEquals("5", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());

        assertEquals(TokenType.Comment, tokens.get(++count).getType());
        assertEquals("#REVIEW a equal to 5", tokens.get(count).getValue());
        assertEquals(2, tokens.get(count).getRow());
    }

}