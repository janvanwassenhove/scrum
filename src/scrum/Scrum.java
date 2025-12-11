package scrum;

import scrum.exception.ImpedimentFormatter;
import scrum.exception.ScrumRuntimeException;
import scrum.exception.SyntaxException;
import scrum.exception.TokenException;

import java.nio.file.Path;

public class Scrum {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: scrum <filename>");
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
}
