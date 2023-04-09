package scrum;

import scrum.context.MemoryContext;
import scrum.context.definition.DefinitionContext;
import scrum.expression.ClassExpression;
import scrum.expression.Expression;
import scrum.expression.VariableExpression;
import scrum.expression.operator.*;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.NumericValue;
import scrum.expression.value.TextValue;
import scrum.statement.*;
import scrum.token.Token;
import scrum.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatementParserTest {

    @Test
    public void printTest() {
        List<Token> tokens = List.of(
                Token.builder().type(TokenType.Keyword).value("SAY").build(),
                Token.builder().type(TokenType.Text).value("Hello World").build()
        );
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        CompositeStatement statement = new CompositeStatement();
        StatementParser.parse(tokens, statement);

        List<Statement> statements = statement.getStatements2Execute();
        assertEquals(1, statements.size());

        assertEquals(SayStatement.class, statements.get(0).getClass());
        SayStatement sayStatement = (SayStatement) statements.get(0);

        assertEquals(TextValue.class, sayStatement.expression().getClass());
        TextValue textValue = (TextValue) sayStatement.expression();

        assertEquals("Hello World", textValue.getValue());

        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

    @Test
    public void testInput() {
        List<Token> tokens = List.of(
                Token.builder().type(TokenType.Keyword).value("ASK").build(),
                Token.builder().type(TokenType.Variable).value("a").build()
        );
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        CompositeStatement statement = new CompositeStatement();
        StatementParser.parse(tokens, statement);

        List<Statement> statements = statement.getStatements2Execute();
        assertEquals(1, statements.size());

        assertEquals(InputStatement.class, statements.get(0).getClass());
        InputStatement inputStatement = (InputStatement) statements.get(0);

        assertEquals("a", inputStatement.name());

        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

    @Test
    public void testAssignment() {
        List<Token> tokens = List.of(
                Token.builder().type(TokenType.Variable).value("a").build(),
                Token.builder().type(TokenType.Operator).value("IS").build(),
                Token.builder().type(TokenType.Numeric).value("2").build(),
                Token.builder().type(TokenType.Operator).value("+").build(),
                Token.builder().type(TokenType.Numeric).value("5").build()
        );
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        CompositeStatement statement = new CompositeStatement();
        StatementParser.parse(tokens, statement);

        List<Statement> statements = statement.getStatements2Execute();
        assertEquals(1, statements.size());

        assertEquals(ExpressionStatement.class, statements.get(0).getClass());
        ExpressionStatement expressionStatement = (ExpressionStatement) statements.get(0);

        assertEquals(expressionStatement.getExpression().getClass(), AssignmentOperator.class);
        AssignmentOperator assignOperator = (AssignmentOperator) expressionStatement.getExpression();

        assertTrue(assignOperator.getLeft() instanceof VariableExpression);
        VariableExpression variableExpression = (VariableExpression) assignOperator.getLeft();
        assertEquals("a", variableExpression.getName());

        assertEquals(AdditionOperator.class, assignOperator.getRight().getClass());
        AdditionOperator operator = (AdditionOperator) assignOperator.getRight();

        assertEquals(NumericValue.class, operator.getLeft().getClass());
        NumericValue left = (NumericValue) operator.getLeft();
        assertEquals(2, left.getValue());

        assertEquals(NumericValue.class, operator.getRight().getClass());
        NumericValue right = (NumericValue) operator.getRight();
        assertEquals(5, right.getValue());

        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

    @Test
    public void testCondition() {
        List<Token> tokens = List.of(
                Token.builder().type(TokenType.Keyword).value("IF").build(),
                Token.builder().type(TokenType.Variable).value("a").build(),
                Token.builder().type(TokenType.Operator).value(">").build(),
                Token.builder().type(TokenType.Numeric).value("5").build(),
                Token.builder().type(TokenType.Keyword).value("SAY").build(),
                Token.builder().type(TokenType.Text).value("a is greater than 5").build(),
                Token.builder().type(TokenType.Keyword).value("ELSEIF").build(),
                Token.builder().type(TokenType.Variable).value("a").build(),
                Token.builder().type(TokenType.Operator).value(">=").build(),
                Token.builder().type(TokenType.Numeric).value("1").build(),
                Token.builder().type(TokenType.Keyword).value("SAY").build(),
                Token.builder().type(TokenType.Text).value("a is greater than or equal to 1").build(),
                Token.builder().type(TokenType.Keyword).value("ELSE").build(),
                Token.builder().type(TokenType.Keyword).value("SAY").build(),
                Token.builder().type(TokenType.Text).value("a is less than 1").build(),
                Token.builder().type(TokenType.Keyword).value("END IF").build()
        );
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        CompositeStatement statement = new CompositeStatement();
        StatementParser.parse(tokens, statement);

        List<Statement> statements = statement.getStatements2Execute();
        assertEquals(1, statements.size());

        assertEquals(ConditionStatement.class, statements.get(0).getClass());
        ConditionStatement conditionStatement = (ConditionStatement) statements.get(0);

        Map<Expression, CompositeStatement> cases = conditionStatement.getCases();
        assertEquals(3, cases.size());

        List<Expression> conditions = new ArrayList<>(cases.keySet());

        //if case
        assertEquals(GreaterThanOperator.class, conditions.get(0).getClass());
        GreaterThanOperator ifCondition = (GreaterThanOperator) conditions.get(0);

        assertTrue(ifCondition.getLeft() instanceof VariableExpression);
        VariableExpression ifLeftExpression = (VariableExpression) ifCondition.getLeft();
        assertEquals("a", ifLeftExpression.getName());

        NumericValue ifRightExpression = (NumericValue) ifCondition.getRight();
        assertEquals(5, ifRightExpression.getValue());

        List<Statement> ifStatements = cases.get(ifCondition).getStatements2Execute();
        assertEquals(1, ifStatements.size());

        assertEquals(SayStatement.class, ifStatements.get(0).getClass());
        SayStatement ifSayStatement = (SayStatement) ifStatements.get(0);

        assertEquals(TextValue.class, ifSayStatement.expression().getClass());
        TextValue ifPrintValue = (TextValue) ifSayStatement.expression();
        assertEquals("a is greater than 5", ifPrintValue.getValue());

        //elif case
        assertEquals(GreaterThanOrEqualToOperator.class, conditions.get(1).getClass());
        GreaterThanOrEqualToOperator elifCondition = (GreaterThanOrEqualToOperator) conditions.get(1);

        assertTrue(elifCondition.getLeft() instanceof VariableExpression);
        VariableExpression elifLeftExpression = (VariableExpression) elifCondition.getLeft();
        assertEquals("a", elifLeftExpression.getName());

        NumericValue elifRightExpression = (NumericValue) elifCondition.getRight();
        assertEquals(1, elifRightExpression.getValue());

        List<Statement> elifStatements = cases.get(elifCondition).getStatements2Execute();
        assertEquals(1, elifStatements.size());

        assertEquals(SayStatement.class, elifStatements.get(0).getClass());
        SayStatement elifSayStatement = (SayStatement) elifStatements.get(0);

        assertEquals(TextValue.class, elifSayStatement.expression().getClass());
        TextValue elifPrintValue = (TextValue) elifSayStatement.expression();
        assertEquals("a is greater than or equal to 1", elifPrintValue.getValue());

        //else case
        assertEquals(LogicalValue.class, conditions.get(2).getClass());
        LogicalValue elseCondition = (LogicalValue) conditions.get(2);

        assertEquals(true, elseCondition.getValue());

        List<Statement> elseStatements = cases.get(elseCondition).getStatements2Execute();
        assertEquals(1, elseStatements.size());

        assertEquals(SayStatement.class, elseStatements.get(0).getClass());
        SayStatement elseSayStatement = (SayStatement) elseStatements.get(0);

        assertEquals(TextValue.class, elseSayStatement.expression().getClass());
        TextValue elsePrintValue = (TextValue) elseSayStatement.expression();
        assertEquals("a is less than 1", elsePrintValue.getValue());

        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

    @Test
    public void testClass() {
        List<Token> tokens = List.of(
                Token.builder().type(TokenType.Keyword).value("EPIC").row(1).build(),
                Token.builder().type(TokenType.Text).value("\"Person\"").row(1).build(),
                Token.builder().type(TokenType.GroupDivider).value("USING [").row(1).build(),
                Token.builder().type(TokenType.Variable).value("name").row(1).build(),
                Token.builder().type(TokenType.GroupDivider).value(",").row(1).build(),
                Token.builder().type(TokenType.Variable).value("age").row(1).build(),
                Token.builder().type(TokenType.GroupDivider).value("]").row(1).build(),
                Token.builder().type(TokenType.LineBreak).value("\n").row(1).build(),
                Token.builder().type(TokenType.Keyword).value("END OF EPIC").row(2).build(),
                Token.builder().type(TokenType.LineBreak).value("\n").row(2).build(),
                Token.builder().type(TokenType.Variable).value("person").row(3).build(),
                Token.builder().type(TokenType.Operator).value("IS").row(3).build(),
                Token.builder().type(TokenType.Operator).value("NEW").row(3).build(),
                Token.builder().type(TokenType.Variable).value("Person").row(3).build(),
                Token.builder().type(TokenType.GroupDivider).value("USING [").row(3).build(),
                Token.builder().type(TokenType.Text).value("mITy John").row(3).build(),
                Token.builder().type(TokenType.GroupDivider).value(",").row(3).build(),
                Token.builder().type(TokenType.Numeric).value("45").row(3).build(),
                Token.builder().type(TokenType.GroupDivider).value("]").row(3).build(),
                Token.builder().type(TokenType.LineBreak).value("\n").row(3).build(),
                Token.builder().type(TokenType.Keyword).value("SAY").row(4).build(),
                Token.builder().type(TokenType.Variable).value("person").row(4).build(),
                Token.builder().type(TokenType.Operator).value("::").row(4).build(),
                Token.builder().type(TokenType.Variable).value("name").row(4).build(),
                Token.builder().type(TokenType.Operator).value("+").row(4).build(),
                Token.builder().type(TokenType.Text).value(" is ").row(4).build(),
                Token.builder().type(TokenType.Operator).value("+").row(4).build(),
                Token.builder().type(TokenType.Variable).value("person").row(4).build(),
                Token.builder().type(TokenType.Operator).value("::").row(4).build(),
                Token.builder().type(TokenType.Variable).value("age").row(4).build(),
                Token.builder().type(TokenType.Operator).value("+").row(4).build(),
                Token.builder().type(TokenType.Text).value(" years old").row(4).build()
        );
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        CompositeStatement statement = new CompositeStatement();
        StatementParser.parse(tokens, statement);

        List<Statement> statements = statement.getStatements2Execute();
        assertEquals(1, statements.size());

        // 1st statement
        assertEquals(ExpressionStatement.class, statements.get(0).getClass());
        ExpressionStatement expressionStatement = (ExpressionStatement) statements.get(0);

        assertEquals(expressionStatement.getExpression().getClass(), AssignmentOperator.class);
        AssignmentOperator assignStatement = (AssignmentOperator) expressionStatement.getExpression();

        assertTrue(assignStatement.getLeft() instanceof VariableExpression);
        VariableExpression variableExpression = (VariableExpression) assignStatement.getLeft();
        assertEquals("person", variableExpression.getName());

        assertEquals(ClassInstanceOperator.class, assignStatement.getRight().getClass());
        ClassInstanceOperator instanceOperator = (ClassInstanceOperator) assignStatement.getRight();

        assertEquals(ClassExpression.class, instanceOperator.getValue().getClass());
        ClassExpression type = (ClassExpression) instanceOperator.getValue();

        assertEquals("Person", type.getName());

        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

    @Test
    public void testComment() {
        List<Token> tokens = List.of(
                Token.builder().type(TokenType.Comment).value("REVIEW a is 5").build(),
                Token.builder().type(TokenType.LineBreak).value("\n").build(),
                Token.builder().type(TokenType.Variable).value("a").build(),
                Token.builder().type(TokenType.Operator).value("IS").build(),
                Token.builder().type(TokenType.Numeric).value("5").build(),
                Token.builder().type(TokenType.Comment).value("note a is set to 5").build()
        );
        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        CompositeStatement statement = new CompositeStatement();
        StatementParser.parse(tokens, statement);

        List<Statement> statements = statement.getStatements2Execute();
        assertEquals(1, statements.size());

        assertEquals(ExpressionStatement.class, statements.get(0).getClass());
        ExpressionStatement expressionStatement = (ExpressionStatement) statements.get(0);

        assertEquals(expressionStatement.getExpression().getClass(), AssignmentOperator.class);
        AssignmentOperator assignStatement = (AssignmentOperator) expressionStatement.getExpression();

        assertTrue(assignStatement.getLeft() instanceof VariableExpression);
        VariableExpression variableExpression = (VariableExpression) assignStatement.getLeft();
        assertEquals("a", variableExpression.getName());
        assertEquals(NumericValue.class, assignStatement.getRight().getClass());
        NumericValue numericValue = (NumericValue) assignStatement.getRight();

        assertEquals(5, numericValue.getValue());

        DefinitionContext.endScope();
        MemoryContext.endScope();
    }

}