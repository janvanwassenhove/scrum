package scrum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.context.definition.ClassDefinition;
import scrum.context.definition.DefinitionContext;
import scrum.context.definition.DefinitionScope;
import scrum.context.definition.FunctionDefinition;
import scrum.exception.SyntaxException;
import scrum.expression.Expression;
import scrum.expression.ExpressionReader;
import scrum.expression.VariableExpression;
import scrum.expression.operator.OperatorExpression;
import scrum.expression.value.LogicalValue;
import scrum.statement.*;
import scrum.statement.loop.*;
import scrum.token.Token;
import scrum.token.TokenType;
import scrum.token.TokensStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
@Getter
public class StatementParser {
    private final TokensStack tokens;
    private final Scanner scanner;
    private final CompositeStatement compositeStatement;

    public static void parse(StatementParser parent, CompositeStatement compositeStatement, DefinitionScope definitionScope) {
        DefinitionContext.pushScope(definitionScope);
        try {
            StatementParser parser = new StatementParser(parent.getTokens(), parent.getScanner(), compositeStatement);
            while (parser.hasNextStatement()) {
                parser.parseExpression();
            }
        } finally {
            DefinitionContext.endScope();
        }
    }

    public static void parse(List<Token> tokens, CompositeStatement compositeStatement) {
        StatementParser parser = new StatementParser(new TokensStack(tokens), new Scanner(System.in), compositeStatement);
        while (parser.hasNextStatement()) {
            parser.parseExpression();
        }
    }

    private boolean hasNextStatement() {
        if (!tokens.hasNext())
            return false;
        if (tokens.peek(TokenType.Operator, TokenType.Variable, TokenType.This))
            return true;
        if (tokens.peek(TokenType.Keyword)) {
            return !tokens.peek(TokenType.Keyword, "ELSE", "ELSEIF","end", "END OF STORY", "END OF EPIC", "END IF", "END OF ITERATION");
        }
        return false;
    }

    private void parseExpression() {
        Token token = tokens.next(TokenType.Keyword, TokenType.Variable, TokenType.This, TokenType.Operator);
        switch (token.getType()) {
            case Variable:
            case Operator:
            case This:
                parseExpressionStatement();
                break;
            case Keyword:
                parseKeywordStatement(token);
                break;
            default:
                throw new SyntaxException(String.format("Statement can't start with the following lexeme `%s`", token));
        }
    }

    private void parseExpressionStatement() {
        tokens.back(); // go back to read an expression from the beginning
        Expression value = ExpressionReader.readExpression(tokens);
        ExpressionStatement statement = new ExpressionStatement(value);
        compositeStatement.addStatement(statement);
    }

    private void parseKeywordStatement(Token token) {
        switch (token.getValue()) {
            case "SAY":
                parsePrintStatement();
                break;
            case "ASK":
                parseInputStatement();
                break;
            case "IF":
                parseConditionStatement();
                break;
            case "EPIC":
                parseClassDefinition();
                break;
            case "USER STORY":
                parseFunctionDefinition();
                break;
            case "RETURN ANSWER":
                parseReturnStatement();
                break;
            case "I WANT TO ITERATE":
                parseLoopStatement();
                break;
            case "break":
                parseBreakStatement();
                break;
            case "next":
                parseNextStatement();
                break;
            default:
                throw new SyntaxException(String.format("Failed to parse a keyword: %s", token.getValue()));
        }
    }
    private void parseExecutionStatement(){
        Token type = tokens.next(TokenType.Text);
    }
    private void parsePrintStatement() {
        Expression expression = ExpressionReader.readExpression(tokens);
        SayStatement statement = new SayStatement(expression);
        compositeStatement.addStatement(statement);
    }

    private void parseInputStatement() {
        Token variable = tokens.next(TokenType.Variable);
        InputStatement statement = new InputStatement(variable.getValue(), scanner::nextLine);
        compositeStatement.addStatement(statement);
    }

    private void parseConditionStatement() {
        tokens.back();
        ConditionStatement conditionStatement = new ConditionStatement();

        while (!tokens.peek(TokenType.Keyword, "END IF")) {
            //read condition case
            Token type = tokens.next(TokenType.Keyword, "IF", "ELSEIF",  "ELSE");
            Expression caseCondition;
            if (type.getValue().equals("ELSE")) {
                caseCondition = new LogicalValue(true); //else case does not have the condition
            } else {
                caseCondition = ExpressionReader.readExpression(tokens);
            }

            //read case statements
            CompositeStatement caseStatement = new CompositeStatement();
            DefinitionScope caseScope = DefinitionContext.newScope();
            StatementParser.parse(this, caseStatement, caseScope);

            //add case
            conditionStatement.addCase(caseCondition, caseStatement);
        }
        tokens.next(TokenType.Keyword, "END IF");

        compositeStatement.addStatement(conditionStatement);
    }

    private void parseClassDefinition() {
        List<String> arguments = new ArrayList<>();
        String epicName = tokens.next(TokenType.Text).getValue().replace(" ", "_");

        if (tokens.peek(TokenType.GroupDivider, "USING [")) {

            tokens.next(TokenType.GroupDivider, "USING ["); //skip open square bracket

            while (!tokens.peek(TokenType.GroupDivider, "]")) {
                Token argumentToken = tokens.next(TokenType.Variable);
                arguments.add(argumentToken.getValue());

                if (tokens.peek(TokenType.GroupDivider, ","))
                    tokens.next();
            }

            tokens.next(TokenType.GroupDivider, "]"); //skip close square bracket
        }

        // add class definition
        ClassStatement classStatement = new ClassStatement();
        DefinitionScope classScope = DefinitionContext.newScope();
        ClassDefinition classDefinition = new ClassDefinition(epicName, arguments, classStatement, classScope);
        DefinitionContext.getScope().addClass(classDefinition);

        //parse class statements
        StatementParser.parse(this, classStatement, classScope);
        tokens.next(TokenType.Keyword, "END OF EPIC");
    }

    private void parseFunctionDefinition() {
        DefinitionContext.getScope().getParent();
        Token type = tokens.next(TokenType.Text);
        String userStoryName = type.getValue().replace(" ", "_");
        List<String> arguments = new ArrayList<>();

        if (tokens.peek(TokenType.GroupDivider, "USING [")) {

            tokens.next(TokenType.GroupDivider, "USING ["); //skip open square bracket

            while (!tokens.peek(TokenType.GroupDivider, "]")) {
                Token argumentToken = tokens.next(TokenType.Variable);
                arguments.add(argumentToken.getValue());

                if (tokens.peek(TokenType.GroupDivider, ","))
                    tokens.next();
            }

            tokens.next(TokenType.GroupDivider, "]"); //skip close square bracket
        }

        //add function definition
        FunctionStatement functionStatement = new FunctionStatement();
        DefinitionScope functionScope = DefinitionContext.newScope();

        FunctionDefinition functionDefinition = new FunctionDefinition(type.getValue(), arguments, functionStatement, functionScope);
        DefinitionContext.getScope().addFunction(functionDefinition);

        //parse function statements
        StatementParser.parse(this, functionStatement, functionScope);
        tokens.next(TokenType.Keyword, "END OF STORY");
    }

    private void parseReturnStatement() {
        Expression expression = ExpressionReader.readExpression(tokens);
        ReturnStatement statement = new ReturnStatement(expression);
        compositeStatement.addStatement(statement);
    }

    private void parseLoopStatement() {
        Expression loopExpression = ExpressionReader.readExpression(tokens);
        if (loopExpression instanceof OperatorExpression || loopExpression instanceof VariableExpression) {
            AbstractLoopStatement loopStatement;

            if (loopExpression instanceof VariableExpression && tokens.peek(TokenType.Keyword, "FOR RANGE")) {
                // loop <variable> in <bounds>
                VariableExpression variable = (VariableExpression) loopExpression;
                tokens.next(TokenType.Keyword, "FOR RANGE");
                Expression bounds = ExpressionReader.readExpression(tokens);

                if (tokens.peek(TokenType.GroupDivider, "TILL")) {
                    // loop <variable> in <lower_bound>..<upper_bound>
                    tokens.next(TokenType.GroupDivider, "TILL");
                    Expression upperBound = ExpressionReader.readExpression(tokens);

                    if (tokens.peek(TokenType.Keyword, "by")) {
                        // loop <variable> in <lower_bound>..<upper_bound> by <step>
                        tokens.next(TokenType.Keyword, "by");
                        Expression step = ExpressionReader.readExpression(tokens);
                        loopStatement = new ForLoopStatement(variable, bounds, upperBound, step);
                    } else {
                        // use default step
                        // loop <variable> in <lower_bound>..<upper_bound>
                        loopStatement = new ForLoopStatement(variable, bounds, upperBound);
                    }

                } else {
                    // loop <variable> in <iterable>
                    loopStatement = new IterableLoopStatement(variable, bounds);
                }

            } else {
                // loop <condition>
                loopStatement = new WhileLoopStatement(loopExpression);
            }

            DefinitionScope loopScope = DefinitionContext.newScope();
            StatementParser.parse(this, loopStatement, loopScope);
            tokens.next(TokenType.Keyword, "END OF ITERATION");

            compositeStatement.addStatement(loopStatement);
        }

    }

    private void parseBreakStatement() {
        BreakStatement statement = new BreakStatement();
        compositeStatement.addStatement(statement);
    }

    private void parseNextStatement() {
        NextStatement statement = new NextStatement();
        compositeStatement.addStatement(statement);
    }
}
