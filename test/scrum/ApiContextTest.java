package scrum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrum.context.ApiContext;
import scrum.context.definition.ApiDefinition;
import scrum.context.definition.DefinitionContext;
import scrum.context.definition.DefinitionScope;
import scrum.expression.value.HttpResponseValue;
import scrum.expression.value.Value;
import scrum.statement.CompositeStatement;
import scrum.token.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiContextTest {

    @BeforeEach
    public void setUp() {
        // Initialize definition context for each test
        DefinitionContext.pushScope(new DefinitionScope(null));
        // Clear API registry
        ApiContext.clearRegistry();
    }

    @AfterEach
    public void tearDown() {
        // Clean up definition context after each test
        DefinitionContext.endScope();
        // Clear API registry
        ApiContext.clearRegistry();
    }

    @Test
    public void testRegisterAndRetrieveApi() {
        String source = """
            I WANT TO DEFINE API "TestApi"
                BASE IS "/api/test"
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("TestApi");
        assertNotNull(apiDef);
        
        // Register API
        ApiContext.registerApi(apiDef);
        
        // Retrieve API
        ApiDefinition retrieved = ApiContext.getApi("TestApi");
        assertNotNull(retrieved);
        assertEquals("TestApi", retrieved.getName());
        assertEquals("/api/test", retrieved.getBasePath());
    }

    @Test
    public void testPathParameterExtraction() {
        String source = """
            I WANT TO DEFINE API "UserApi"
                BASE IS "/api/users"
                
                I WANT TO DEFINE ENDPOINT "GetUser"
                    METHOD IS "GET"
                    PATH IS "/user/{id}"
                    
                    WHEN REQUEST
                        temp IS 123
                    END WHEN
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        program.execute();
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("UserApi");
        assertNotNull(apiDef);
        
        // Register API
        ApiContext.registerApi(apiDef);
        
        // Invoke endpoint with path parameter
        Map<String, String> queryParams = new HashMap<>();
        Value<?> response = ApiContext.invokeEndpoint("UserApi", "/user/42", queryParams, null);
        
        assertNotNull(response);
        assertTrue(response instanceof HttpResponseValue);
    }

    @Test
    public void testMultiplePathParameters() {
        String source = """
            I WANT TO DEFINE API "OrderApi"
                BASE IS "/api/orders"
                
                I WANT TO DEFINE ENDPOINT "GetOrder"
                    METHOD IS "GET"
                    PATH IS "/customer/{customerId}/order/{orderId}"
                    
                    WHEN REQUEST
                        status IS "OK"
                    END WHEN
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        program.execute();
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("OrderApi");
        assertNotNull(apiDef);
        
        // Register API
        ApiContext.registerApi(apiDef);
        
        // Invoke endpoint with multiple path parameters
        Map<String, String> queryParams = new HashMap<>();
        Value<?> response = ApiContext.invokeEndpoint("OrderApi", "/customer/100/order/5000", queryParams, null);
        
        assertNotNull(response);
        assertTrue(response instanceof HttpResponseValue);
        HttpResponseValue httpResponse = (HttpResponseValue) response;
        assertEquals(200, httpResponse.getStatusCode());
    }

    @Test
    public void testRequestContextManagement() {
        // Set request context
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("test", "value");
        ApiContext.setRequestContext(requestData);
        
        // Verify context was set
        Map<String, Object> retrieved = ApiContext.getCurrentRequest();
        assertNotNull(retrieved);
        assertEquals("value", retrieved.get("test"));
        
        // Clear context
        ApiContext.clearRequestContext();
        
        // Verify context was cleared
        Map<String, Object> afterClear = ApiContext.getCurrentRequest();
        assertTrue(afterClear.isEmpty());
    }

    @Test
    public void testInvokeNonExistentApi() {
        Map<String, String> queryParams = new HashMap<>();
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ApiContext.invokeEndpoint("NonExistentApi", "/test", queryParams, null);
        });
        
        assertTrue(exception.getMessage().contains("API not found"));
    }

    @Test
    public void testInvokeNonMatchingPath() {
        String source = """
            I WANT TO DEFINE API "TestApi"
                BASE IS "/api"
                
                I WANT TO DEFINE ENDPOINT "Test"
                    METHOD IS "GET"
                    PATH IS "/hello/{name}"
                    
                    WHEN REQUEST
                        temp IS 1
                    END WHEN
                END OF ENDPOINT
            END OF API
            """;
        
        LexicalParser lexer = new LexicalParser(source);
        List<Token> tokens = lexer.parse();
        
        CompositeStatement program = new CompositeStatement();
        StatementParser.parse(tokens, program);
        program.execute();
        
        ApiDefinition apiDef = DefinitionContext.getScope().getApi("TestApi");
        ApiContext.registerApi(apiDef);
        
        Map<String, String> queryParams = new HashMap<>();
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ApiContext.invokeEndpoint("TestApi", "/goodbye/world", queryParams, null);
        });
        
        assertTrue(exception.getMessage().contains("No matching endpoint found"));
    }
}
