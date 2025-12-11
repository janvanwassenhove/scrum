package scrum;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the Scrum programming language implementation.
 * 
 * This class contains comprehensive integration tests that verify the execution
 * of various Scrum language example programs including:
 * - Basic console output and input handling
 * - Control flow and conditional logic
 * - Data structure operations and algorithms
 * - API definition and endpoint simulation
 * - Interactive user input scenarios
 * 
 * Each test executes a complete Scrum program file and validates the expected
 * output against actual execution results.
 */
class ScrumLanguageTest {

    public static final String BASE_PATH = "development/examples/";
    public static final String HELLO_WORLD_SCRUM = BASE_PATH + "HelloWorld.scrum";
    public static final String TEXT_INPUT_SCRUM = BASE_PATH + "TextInput.scrum";
    public static final String ODD_OR_NOT_SCRUM = BASE_PATH + "OddOrNot.scrum";
    public static final String SEARCH_BACKLOG_SCRUM = BASE_PATH + "SearchBacklog.scrum";
    public static final String API_EXAMPLE_SCRUM = BASE_PATH + "ApiExample.scrum";
    public static final String EXECUTABLE_API_EXAMPLE_SCRUM = BASE_PATH + "ExecutableApiExample.scrum";
    public static final String SIMPLE_AGE_API_SCRUM = BASE_PATH + "SimpleAgeApi.scrum";
    public static final String AGE_CALCULATOR_API_SCRUM = BASE_PATH + "AgeCalculatorApi.scrum";

    /**
     * Hello World example, will simply print out 'Hello world!' to the console.
     */
    @Test
    void helloWorldStoryTest() throws URISyntaxException, IOException {
        runScrumCodeWithoutInput(HELLO_WORLD_SCRUM, "Hello world!\r\n");
    }

    @Test
    void textInputTest() throws URISyntaxException, IOException {
        String input = "Writing Scrum!";
        String output =   "Enter \"textForInput\" >>> "+input+"\r\n";
        runScrumCodeWithInput(TEXT_INPUT_SCRUM,input, output);
    }

    @Test
    void oddOrNotTest() throws URISyntaxException, IOException {
        String input = "5";
        String output =   "Enter \"numberForInput\" >>> "+input+" is an odd number!\r\n";
        runScrumCodeWithInput(ODD_OR_NOT_SCRUM,input, output);

        input = "10";
        output =   "Enter \"numberForInput\" >>> "+input+" is an even number!\r\n";
        runScrumCodeWithInput(ODD_OR_NOT_SCRUM,input, output);
    }

    @Test
    void searchBacklogTest() throws URISyntaxException, IOException {
        runScrumCodeWithoutInput(SEARCH_BACKLOG_SCRUM, "[-1, 40, -3, 20]\r\n" +
                "false\r\n" +
                "[-3, -1, 20, 40]\r\n" +
                "true\r\n");
    }

    /**
     * API Example - Demonstrates API definitions with multiple endpoints
     */
    @Test
    void apiExampleTest() throws URISyntaxException, IOException {
        runScrumCodeWithoutInput(API_EXAMPLE_SCRUM, "API definitions loaded successfully!\r\n");
    }

    /**
     * Executable API Example - Demonstrates executable endpoints with WHEN REQUEST blocks
     */
    @Test
    void executableApiExampleTest() throws URISyntaxException, IOException {
        String expectedOutput = "Greeting API with executable endpoints defined successfully!\r\n" +
                "\r\n" +
                "The API has 2 endpoints with WHEN REQUEST blocks:\r\n" +
                "  1. GET /hello/{name}\r\n" +
                "  2. GET /welcome/{user}/{role}\r\n" +
                "\r\n" +
                "Each endpoint contains executable logic that runs when invoked.\r\n";
        runScrumCodeWithoutInput(EXECUTABLE_API_EXAMPLE_SCRUM, expectedOutput);
    }

    /**
     * Simple Age API - Interactive age calculator with formatted output
     */
    @Test
    void simpleAgeApiTest() throws URISyntaxException, IOException {
        String input = "1990";
        String expectedOutput = "┌─────────────────────────────────────┐\r\n" +
                "│   Age Calculator API v1.0           │\r\n" +
                "│   Endpoint: GET /api/age/{year}     │\r\n" +
                "└─────────────────────────────────────┘\r\n" +
                "\r\n" +
                "Please enter your birth year:\r\n" +
                "Enter \"birthYear\" >>> \r\n" +
                "Calling API endpoint...\r\n" +
                "   GET /api/age/1990\r\n" +
                "\r\n" +
                "API Response received!\r\n" +
                "\r\n" +
                "┌─────────────────────────┐\r\n" +
                "│                         │\r\n" +
                "│      Your Age: 34       │\r\n" +
                "│                         │\r\n" +
                "└─────────────────────────┘\r\n";
        runScrumCodeWithInput(SIMPLE_AGE_API_SCRUM, input, expectedOutput);
    }

    /**
     * Age Calculator API - Full demo with age calculation and life stage determination
     */
    @Test
    void ageCalculatorApiTest() throws URISyntaxException, IOException {
        String input = "1990\n2024";
        String expectedOutput = "=== Age Calculator API Demo ===\r\n" +
                "\r\n" +
                "Enter your birth year:\r\n" +
                "Enter \"userBirthYear\" >>> Enter current year (e.g., 2024):\r\n" +
                "Enter \"userCurrentYear\" >>> \r\n" +
                "--- Simulating API Call: GET /api/calculator/age/\r\n" +
                "1990\r\n" +
                "?currentYear=\r\n" +
                "2024\r\n" +
                "---\r\n" +
                "\r\n" +
                "Birth Year: \r\n" +
                "1990\r\n" +
                "Current Year: \r\n" +
                "2024\r\n" +
                "Calculated Age: \r\n" +
                "34\r\n" +
                "\r\n" +
                "--- Simulating API Call: GET /api/calculator/lifestage/\r\n" +
                "34\r\n" +
                "---\r\n" +
                "\r\n" +
                "\r\n" +
                "=== API Response ===\r\n" +
                "Age: \r\n" +
                "34\r\n" +
                "Life Stage: \r\n" +
                "Adult\r\n" +
                "\r\n" +
                "Thank you for using the Age Calculator API!\r\n";
        runScrumCodeWithInput(AGE_CALCULATOR_API_SCRUM, input, expectedOutput);
    }

    /**
     * Age Calculator API - Test child life stage
     */
    @Test
    void ageCalculatorApiChildTest() throws URISyntaxException, IOException {
        String input = "2015\n2024";
        runScrumCodeExpectingLifeStage(AGE_CALCULATOR_API_SCRUM, input, "Child");
    }

    /**
     * Age Calculator API - Test teenager life stage
     */
    @Test
    void ageCalculatorApiTeenagerTest() throws URISyntaxException, IOException {
        String input = "2010\n2024";
        runScrumCodeExpectingLifeStage(AGE_CALCULATOR_API_SCRUM, input, "Teenager");
    }

    /**
     * Age Calculator API - Test senior life stage
     */
    @Test
    void ageCalculatorApiSeniorTest() throws URISyntaxException, IOException {
        String input = "1950\n2024";
        runScrumCodeExpectingLifeStage(AGE_CALCULATOR_API_SCRUM, input, "Senior");
    }

    private void runScrumCodeExpectingLifeStage(String scrumCodeFile, String input, String expectedLifeStage) throws URISyntaxException, IOException {
        Path path = Paths.get(scrumCodeFile);

        try (InputStream in = new ByteArrayInputStream(input.getBytes());
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(baos)) {

            System.setIn(in);
            System.setOut(out);

            ScrumLanguage lang = new ScrumLanguage();
            lang.execute(path);

            String output = baos.toString();
            assertEquals(true, output.contains("Life Stage: \r\n" + expectedLifeStage),
                    "Expected life stage '" + expectedLifeStage + "' but got output: " + output);
        }
    }

    private void runScrumCodeWithInput(String scrumCodeFile, String input, String output) throws URISyntaxException, IOException {
        Path path = Paths.get(scrumCodeFile);

        try (InputStream in = new ByteArrayInputStream(input.getBytes());
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(baos)) {

            System.setIn(in);
            System.setOut(out);

            ScrumLanguage lang = new ScrumLanguage();
            lang.execute(path);

            assertEquals(output, baos.toString());
        }
    }

    private void runScrumCodeWithoutInput(String scrumCodeFile, String output) throws URISyntaxException, IOException {
        Path path = Paths.get(scrumCodeFile);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(baos)) {
            System.setOut(out);

            ScrumLanguage lang = new ScrumLanguage();
            lang.execute(path);

            assertEquals(output, baos.toString());
        }
    }
}