package scrum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.context.definition.ClassDefinition;
import scrum.context.definition.DefinitionContext;
import scrum.context.definition.DefinitionScope;
import scrum.context.definition.FunctionDefinition;
import scrum.context.definition.ApiDefinition;
import scrum.context.definition.EndpointDefinition;
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
            return !tokens.peek(TokenType.Keyword, "ELSE", "ELSEIF","end", "END OF STORY", "END OF EPIC", "END IF", "END OF ITERATION", "END OF API", "END OF ENDPOINT", "END WHEN");
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
            case "I WANT TO DEFINE":
                parseDefinition();
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
    @SuppressWarnings("unused")
    private void parseExecutionStatement(){
        @SuppressWarnings("unused")
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
        @SuppressWarnings("unused")
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

    private void parseDefinition() {
        Token nextToken = tokens.next(TokenType.Keyword);
        if (nextToken.getValue().equals("API")) {
            parseApiDefinition();
        } else if (nextToken.getValue().equals("ENDPOINT")) {
            parseEndpointDefinition();
        } else {
            throw new SyntaxException(String.format("Expected API or ENDPOINT after 'I WANT TO DEFINE', got: %s", nextToken.getValue()));
        }
    }

    private void parseApiDefinition() {
        String apiName = tokens.next(TokenType.Text).getValue().replace(" ", "_");
        
        // Parse BASE IS "path"
        String basePath = "";
        if (tokens.peek(TokenType.Keyword, "BASE")) {
            tokens.next(TokenType.Keyword, "BASE");
            tokens.next(TokenType.Operator, "IS");
            basePath = tokens.next(TokenType.Text).getValue();
        }
        
        // Create API definition
        ApiStatement apiStatement = new ApiStatement();
        DefinitionScope apiScope = DefinitionContext.newScope();
        ApiDefinition apiDefinition = new ApiDefinition(apiName, basePath, apiStatement, apiScope);
        DefinitionContext.getScope().addApi(apiDefinition);
        
        // Parse API body (including nested endpoints)
        StatementParser.parse(this, apiStatement, apiScope);
        
        tokens.next(TokenType.Keyword, "END OF API");
    }

    private void parseEndpointDefinition() {
        String endpointName = tokens.next(TokenType.Text).getValue().replace(" ", "_");
        
        String method = "";
        String path = "";
        String queryParams = "";
        String returnType = "";
        
        // Parse endpoint properties
        while (!tokens.peek(TokenType.Keyword, "END OF ENDPOINT") && !tokens.peek(TokenType.Keyword, "WHEN")) {
            if (tokens.peek(TokenType.Keyword, "METHOD")) {
                tokens.next(TokenType.Keyword, "METHOD");
                tokens.next(TokenType.Operator, "IS");
                method = tokens.next(TokenType.Text).getValue();
            } else if (tokens.peek(TokenType.Keyword, "PATH")) {
                tokens.next(TokenType.Keyword, "PATH");
                tokens.next(TokenType.Operator, "IS");
                path = tokens.next(TokenType.Text).getValue();
            } else if (tokens.peek(TokenType.Keyword, "QUERY_PARAMS")) {
                tokens.next(TokenType.Keyword, "QUERY_PARAMS");
                // Handle both "IS" and "ARE"
                Token operator = tokens.next(TokenType.Operator, TokenType.Keyword);
                if (!operator.getValue().equals("IS") && !operator.getValue().equals("ARE")) {
                    throw new SyntaxException("Expected IS or ARE after QUERY_PARAMS");
                }
                // Parse array syntax { "param1", "param2" }
                tokens.next(TokenType.GroupDivider, "{");
                StringBuilder params = new StringBuilder();
                while (!tokens.peek(TokenType.GroupDivider, "}")) {
                    if (params.length() > 0) {
                        params.append(", ");
                    }
                    params.append(tokens.next(TokenType.Text).getValue());
                    if (tokens.peek(TokenType.GroupDivider, ",")) {
                        tokens.next();
                    }
                }
                tokens.next(TokenType.GroupDivider, "}");
                queryParams = params.toString();
            } else if (tokens.peek(TokenType.Keyword, "RETURNS")) {
                tokens.next(TokenType.Keyword, "RETURNS");
                tokens.next(TokenType.Operator, "IS");
                returnType = tokens.next(TokenType.Text).getValue();
            } else {
                Token unexpectedToken = tokens.next(TokenType.Keyword, TokenType.Operator, TokenType.Text);
                throw new SyntaxException(String.format("Unexpected token in endpoint definition: %s", unexpectedToken.getValue()));
            }
        }
        
        // Check for WHEN REQUEST block (executable endpoint)
        if (tokens.peek(TokenType.Keyword, "WHEN")) {
            tokens.next(TokenType.Keyword, "WHEN");
            tokens.next(TokenType.Keyword, "REQUEST");
            
            // Create executable endpoint with handler body
            List<String> queryParamsList = new ArrayList<>();
            if (!queryParams.isEmpty()) {
                for (String param : queryParams.split(", ")) {
                    queryParamsList.add(param);
                }
            }
            
            // Create a new definition scope for the endpoint handler
            DefinitionScope handlerScope = DefinitionContext.newScope();
            
            // Create executable endpoint statement
            ExecutableEndpointStatement executableEndpoint = new ExecutableEndpointStatement(
                endpointName, method, path, queryParams, returnType, handlerScope
            );
            
            // Parse statements within WHEN REQUEST block
            StatementParser.parse(this, executableEndpoint, handlerScope);
            
            tokens.next(TokenType.Keyword, "END WHEN");
            tokens.next(TokenType.Keyword, "END OF ENDPOINT");
            
            compositeStatement.addStatement(executableEndpoint);
            
            // Create endpoint definition
            @SuppressWarnings("unused")
            EndpointDefinition endpointDefinition = new EndpointDefinition(
                endpointName, method, path, queryParamsList, returnType
            );
        } else {
            // Declarative endpoint (no handler)
            tokens.next(TokenType.Keyword, "END OF ENDPOINT");
            
            // Create endpoint statement and add to composite
            EndpointStatement endpointStatement = new EndpointStatement(endpointName, method, path, queryParams, returnType);
            compositeStatement.addStatement(endpointStatement);
            
            // Create endpoint definition and add to current API (if we're inside an API definition)
            List<String> queryParamsList = new ArrayList<>();
            if (!queryParams.isEmpty()) {
                for (String param : queryParams.split(", ")) {
                    queryParamsList.add(param);
                }
            }
            @SuppressWarnings("unused")
            EndpointDefinition endpointDefinition = new EndpointDefinition(endpointName, method, path, queryParamsList, returnType);
            
            // Try to find the parent API definition and add this endpoint to it
            // For now, we'll rely on the execution phase to link endpoints to APIs
            // A more sophisticated implementation could track the current API context
        }
    }
}
