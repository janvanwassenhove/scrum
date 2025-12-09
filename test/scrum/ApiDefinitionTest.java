package scrum;

import scrum.context.definition.ApiDefinition;
import scrum.context.definition.DefinitionContext;
import scrum.context.definition.DefinitionScope;
import scrum.statement.CompositeStatement;
import scrum.token.Token;
import scrum.token.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiDefinitionTest {

    @BeforeEach
    public void setUp() {
        // Initialize definition context for each test
        DefinitionContext.pushScope(new DefinitionScope(null));
    }

    @AfterEach
    public void tearDown() {
        // Clean up definition context after each test
        DefinitionContext.endScope();
    }

    @Test
    public void testApiKeywordsLexicalParsing() {
        String source = "I WANT TO DEFINE API \"TestApi\" BASE IS \"/api/test\" END OF API";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        // Verify key tokens are correctly parsed
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("I WANT TO DEFINE")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("API")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("BASE")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("END OF API")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Text && t.getValue().equals("TestApi")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Text && t.getValue().equals("/api/test")));
    }

    @Test
    public void testEndpointKeywordsLexicalParsing() {
        String source = "I WANT TO DEFINE ENDPOINT \"GetUsers\" METHOD IS \"GET\" PATH IS \"/users\" END OF ENDPOINT";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("I WANT TO DEFINE")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("ENDPOINT")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("METHOD")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("PATH")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("END OF ENDPOINT")));
    }

    @Test
    public void testSimpleApiDefinitionParsing() {
        String source = """
            I WANT TO DEFINE API "UserApi"
                BASE IS "/api/users"
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        // Verify API definition was created and stored
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("UserApi");
        assertNotNull(apiDef);
        assertEquals("UserApi", apiDef.getName());
        assertEquals("/api/users", apiDef.getBasePath());
        assertNotNull(apiDef.getStatement());
        assertNotNull(apiDef.getDefinitionScope());
    }

    @Test
    public void testApiWithNestedEndpoints() {
        String source = """
            I WANT TO DEFINE API "InvoiceApi"
                BASE IS "/api/invoices"
                
                I WANT TO DEFINE ENDPOINT "GetInvoices"
                    METHOD IS "GET"
                    PATH IS "/list"
                    RETURNS IS "Invoice[]"
                END OF ENDPOINT
                
                I WANT TO DEFINE ENDPOINT "GetById"
                    METHOD IS "GET"
                    PATH IS "/{id}"
                    QUERY_PARAMS ARE { "includeDetails" }
                    RETURNS IS "Invoice"
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        // Verify API definition exists
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("InvoiceApi");
        assertNotNull(apiDef);
        assertEquals("InvoiceApi", apiDef.getName());
        assertEquals("/api/invoices", apiDef.getBasePath());
        
        // Verify the API statement contains endpoint statements
        assertNotNull(apiDef.getStatement());
        assertTrue(apiDef.getStatement().getStatements2Execute().size() >= 2);
    }

    @Test
    public void testEndpointWithAllProperties() {
        String source = """
            I WANT TO DEFINE API "TestApi"
                BASE IS "/api/test"
                
                I WANT TO DEFINE ENDPOINT "SearchItems"
                    METHOD IS "POST"
                    PATH IS "/search/{category}"
                    QUERY_PARAMS ARE { "page", "limit", "sort" }
                    RETURNS IS "SearchResult"
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("TestApi");
        assertNotNull(apiDef);
        
        // Verify endpoint was parsed
        assertEquals(1, apiDef.getStatement().getStatements2Execute().size());
    }

    @Test
    public void testMultipleApis() {
        String source = """
            I WANT TO DEFINE API "UserApi"
                BASE IS "/api/users"
            END OF API
            
            I WANT TO DEFINE API "ProductApi"
                BASE IS "/api/products"
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        // Verify both APIs exist
        ApiDefinition userApi = DefinitionContext.getScope().getApi("UserApi");
        assertNotNull(userApi);
        assertEquals("/api/users", userApi.getBasePath());
        
        ApiDefinition productApi = DefinitionContext.getScope().getApi("ProductApi");
        assertNotNull(productApi);
        assertEquals("/api/products", productApi.getBasePath());
    }

    @Test
    public void testApiExecutionPrintsEndpoints() {
        String source = """
            I WANT TO DEFINE API "TestApi"
                BASE IS "/api/test"
                
                I WANT TO DEFINE ENDPOINT "TestEndpoint"
                    METHOD IS "GET"
                    PATH IS "/test"
                    RETURNS IS "String"
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        // Execute the program - should not throw exceptions
        assertDoesNotThrow(() -> program.execute());
    }

    @Test
    public void testEndpointWithoutQueryParams() {
        String source = """
            I WANT TO DEFINE API "SimpleApi"
                BASE IS "/api"
                
                I WANT TO DEFINE ENDPOINT "Simple"
                    METHOD IS "DELETE"
                    PATH IS "/{id}"
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        
        // Should parse without errors even without QUERY_PARAMS or RETURNS
        assertDoesNotThrow(() -> StatementParser.parse(tokens, program));
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("SimpleApi");
        assertNotNull(apiDef);
    }

    @Test
    public void testEndpointWithoutReturnType() {
        String source = """
            I WANT TO DEFINE API "VoidApi"
                BASE IS "/api/void"
                
                I WANT TO DEFINE ENDPOINT "DoSomething"
                    METHOD IS "POST"
                    PATH IS "/action"
                    QUERY_PARAMS ARE { "param1" }
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        assertDoesNotThrow(() -> StatementParser.parse(tokens, program));
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("VoidApi");
        assertNotNull(apiDef);
    }

    @Test
    public void testExecutableEndpointWithWhenRequestBlock() {
        String source = """
            I WANT TO DEFINE API "ExecutableApi"
                BASE IS "/api/test"
                
                I WANT TO DEFINE ENDPOINT "TestEndpoint"
                    METHOD IS "GET"
                    PATH IS "/hello/{name}"
                    
                    WHEN REQUEST
                        message IS "Hello from endpoint"
                    END WHEN
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        assertDoesNotThrow(() -> StatementParser.parse(tokens, program));
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("ExecutableApi");
        assertNotNull(apiDef);
        assertNotNull(apiDef.getStatement());
        // Verify the endpoint was added to the API
        assertTrue(apiDef.getStatement().getStatements2Execute().size() > 0);
    }

    @Test
    public void testWhenRequestKeywordsLexicalParsing() {
        String source = "WHEN REQUEST SAY \"test\" END WHEN";
        LexicalParser parser = new LexicalParser(source);
        List<Token> tokens = parser.parse();

        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("WHEN")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("REQUEST")));
        assertTrue(tokens.stream().anyMatch(t -> t.getType() == TokenType.Keyword && t.getValue().equals("END WHEN")));
    }
}
