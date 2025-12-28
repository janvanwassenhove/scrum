package scrum;

import scrum.exception.ImpedimentFormatter;
import scrum.exception.ScrumRuntimeException;
import scrum.exception.SyntaxException;
import scrum.exception.TokenException;

import java.nio.file.Path;

public class Scrum {

    // Version information
    private static final String VERSION = "1.3.0";
    private static final String BUILD_DATE = "2025-12-27";

    public static void main(String[] args) {
        // Handle version flag
        if (args.length > 0 && (args[0].equals("--version") || args[0].equals("-v"))) {
            printVersion();
            System.exit(0);
        }

        if (args.length == 0) {
            System.out.println("Usage: scrum <filename>");
            System.out.println("       scrum --version    Display version information");
            System.exit(1);
        }

        String filename = args[0];
        ScrumLanguage lang = new ScrumLanguage();
        
        try {
            lang.execute(Path.of(filename));
        } catch (ScrumRuntimeException ex) {
            // Scrum-style runtime impediment
            System.err.println(ImpedimentFormatter.formatRuntimeImpediment(ex));
            
            // Optional: print full stack trace if debug flag is set
            if (System.getProperty("scrum.debug") != null) {
                System.err.println("\n--- FULL STACK TRACE (DEBUG MODE) ---");
                ex.printStackTrace();
            }
            System.exit(1);
        } catch (SyntaxException ex) {
            // Scrum-style syntax impediment
            System.err.println(ImpedimentFormatter.formatSyntaxImpediment(ex));
            
            if (System.getProperty("scrum.debug") != null) {
                System.err.println("\n--- FULL STACK TRACE (DEBUG MODE) ---");
                ex.printStackTrace();
            }
            System.exit(1);
        } catch (TokenException ex) {
            // Scrum-style token impediment
            System.err.println(ImpedimentFormatter.formatTokenImpediment(ex));
            
            if (System.getProperty("scrum.debug") != null) {
                System.err.println("\n--- FULL STACK TRACE (DEBUG MODE) ---");
                ex.printStackTrace();
            }
            System.exit(1);
        } catch (Exception ex) {
            // Wrap unexpected exceptions
            System.err.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.err.println("║  SCRUM IMPEDIMENT – UNEXPECTED TECHNICAL BLOCKER               ║");
            System.err.println("╚════════════════════════════════════════════════════════════════╝\n");
            System.err.println("This story was blocked by an unexpected technical impediment.\n");
            System.err.println("IMPEDIMENT CODE: SCRUM-RUNTIME-UNKNOWN-001");
            System.err.println("ROOT CAUSE: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            System.err.println();
            
            if (System.getProperty("scrum.debug") != null) {
                System.err.println("\n--- FULL STACK TRACE (DEBUG MODE) ---");
                ex.printStackTrace();
            }
            System.exit(1);
        }
    }

    /**
     * Print version and environment information
     */
    private static void printVersion() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SCRUM Programming Language                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Version:        " + VERSION);
        System.out.println("Build Date:     " + BUILD_DATE);
        System.out.println();
        System.out.println("Java Version:   " + System.getProperty("java.version"));
        System.out.println("Java Vendor:    " + System.getProperty("java.vendor"));
        System.out.println("Java Home:      " + System.getProperty("java.home"));
        System.out.println();
        
        String scrumHome = System.getenv("SCRUM_HOME");
        if (scrumHome != null) {
            System.out.println("SCRUM_HOME:     " + scrumHome);
        } else {
            System.out.println("SCRUM_HOME:     (not set)");
        }
        System.out.println();
        System.out.println("Copyright (c) 2025 Jan Van Wassenhove");
        System.out.println("License: See LICENSE file in distribution");
    }
}
