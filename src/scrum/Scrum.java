package scrum;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Scrum {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: scrum <filename>");
            System.exit(1);
        }

        String filename = args[0];
        ScrumLanguage lang = new ScrumLanguage();
        lang.execute(Path.of(filename));
    }
}
