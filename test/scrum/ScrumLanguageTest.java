package scrum;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScrumLanguageTest {

    public static final String BASE_PATH = "development/examples/";
    public static final String HELLO_WORLD_SCRUM = BASE_PATH + "HelloWorld.scrum";
    public static final String TEXT_INPUT_SCRUM = BASE_PATH + "TextInput.scrum";
    public static final String ODD_OR_NOT_SCRUM = BASE_PATH + "OddOrNot.scrum";
    public static final String SEARCH_BACKLOG_SCRUM = BASE_PATH + "SearchBacklog.scrum";

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