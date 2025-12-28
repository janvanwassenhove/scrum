package scrum;

import scrum.exception.ImpedimentFormatter;
import scrum.exception.ScrumRuntimeException;
import scrum.exception.SyntaxException;
import scrum.exception.TokenException;

import java.nio.file.Path;
import java.util.List;

public class Scrum {

    // Version information
    private static final String VERSION = "2.0.0";
    private static final String BUILD_DATE = "2025-12-28";

    public static void main(String[] args) {
        // Handle command line options
        if (args.length > 0) {
            String firstArg = args[0];
            
            // Version information
            if (firstArg.equals("--version") || firstArg.equals("-v")) {
                printVersion();
                System.exit(0);
            }
            
            // Help information
            if (firstArg.equals("--help") || firstArg.equals("-h")) {
                printHelp();
                System.exit(0);
            }
            
            // Debug mode
            if (firstArg.equals("--debug") || firstArg.equals("-d")) {
                if (args.length < 2) {
                    System.err.println("Error: --debug requires a filename argument");
                    printUsage();
                    System.exit(1);
                }
                System.setProperty("scrum.debug", "true");
                executeFile(args[1]);
                return;
            }
            
            // Validate syntax only (no execution)
            if (firstArg.equals("--validate") || firstArg.equals("--syntax-check") || firstArg.equals("-c")) {
                if (args.length < 2) {
                    System.err.println("Error: --validate requires a filename argument");
                    printUsage();
                    System.exit(1);
                }
                validateSyntaxOnly(args[1]);
                return;
            }
            
            // Show examples
            if (firstArg.equals("--examples")) {
                showExamples();
                System.exit(0);
            }
        }

        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        // Execute file
        executeFile(args[0]);
    }
    
    /**
     * Print usage information
     */
    private static void printUsage() {
        System.out.println("Usage: scrum [OPTIONS] <filename>");
        System.out.println("");
        System.out.println("OPTIONS:");
        System.out.println("  -v, --version       Display version information");
        System.out.println("  -h, --help          Display this help message");
        System.out.println("  -d, --debug         Enable debug mode with detailed error traces");
        System.out.println("  -c, --validate      Validate syntax only (no execution)");
        System.out.println("      --syntax-check  Alias for --validate");
        System.out.println("      --examples      Show available examples and sample code");
        System.out.println("");
        System.out.println("EXAMPLES:");
        System.out.println("  scrum hello.scrum           Execute a SCRUM program");
        System.out.println("  scrum --debug app.scrum     Execute with detailed debugging");
        System.out.println("  scrum --validate test.scrum Check syntax without running");
        System.out.println("  scrum --examples            View sample code and examples");
    }
    
    /**
     * Print detailed help information
     */
    private static void printHelp() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SCRUM Programming Language - Help                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        printUsage();
        System.out.println();
        System.out.println("SCRUM is an AI-powered programming language featuring:");
        System.out.println("• Business-friendly syntax with natural language constructs");
        System.out.println("• Revolutionary #INTENT for AI-powered natural language programming");
        System.out.println("• Scrum methodology integration (Stories, Epics, Impediments)");
        System.out.println("• Multiple LLM provider support (OpenAI, Claude, Groq, Ollama, etc.)");
        System.out.println();
        System.out.println("For more information:");
        System.out.println("• Documentation: https://github.com/janvanwassenhove/scrum/docs");
        System.out.println("• Examples: Run 'scrum --examples' or check examples/ directory");
        System.out.println("• Issues: https://github.com/janvanwassenhove/scrum/issues");
    }
    
    /**
     * Show available examples and sample code
     */
    private static void showExamples() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SCRUM Programming Language - Examples                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        String scrumHome = System.getenv("SCRUM_HOME");
        if (scrumHome != null) {
            System.out.println("Example files are located in: " + scrumHome + "\\examples\\");
        } else {
            System.out.println("Example files are typically located in: [SCRUM_HOME]\\examples\\");
        }
        System.out.println();
        
        System.out.println("Available Examples:");
        System.out.println("• HelloWorld.scrum           - Basic SCRUM syntax and structure");
        System.out.println("• ApiExample.scrum           - REST API integration examples");
        System.out.println("• IntentComputeBirthYear.scrum - AI-powered #INTENT example");
        System.out.println("• AgeCalculatorApi.scrum     - Combining APIs with business logic");
        System.out.println("• SearchBacklog.scrum        - Scrum methodology integration");
        System.out.println("• OddOrNot.scrum            - Conditional logic and expressions");
        System.out.println("• TextInput.scrum           - User input and interaction");
        System.out.println();
        
        System.out.println("Simple Hello World Example:");
        System.out.println("```scrum");
        System.out.println("story \"Display Welcome Message\" {");
        System.out.println("    print(\"Hello from SCRUM! 👋\");");
        System.out.println("    print(\"Welcome to AI-powered programming!\");");
        System.out.println("}");
        System.out.println("```");
        System.out.println();
        
        System.out.println("AI-Powered #INTENT Example:");
        System.out.println("```scrum");
        System.out.println("story \"Calculate User Age\" {");
        System.out.println("    #INTENT \"Calculate the age of a person born in 1990\"");
        System.out.println("    print(result);");
        System.out.println("}");
        System.out.println("```");
        System.out.println();
        
        System.out.println("Run an example: scrum [example-file.scrum]");
    }
    
    /**
     * Execute a SCRUM file
     */
    private static void executeFile(String filename) {
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
     * Validate syntax only without execution
     */
    private static void validateSyntaxOnly(String filename) {
        try {
            String source = java.nio.file.Files.readString(Path.of(filename));
            String fileName = Path.of(filename).getFileName().toString();
            
            // Initialize execution context for syntax checking
            scrum.context.ExecutionContext.initialize(fileName, source);
            
            // Parse tokens and statements for syntax validation
            scrum.LexicalParser lexicalParser = new scrum.LexicalParser(source);
            List<scrum.token.Token> tokens = lexicalParser.parse();
            
            scrum.context.definition.DefinitionContext.pushScope(scrum.context.definition.DefinitionContext.newScope());
            scrum.statement.CompositeStatement statement = new scrum.statement.CompositeStatement();
            scrum.StatementParser.parse(tokens, statement);
            
            System.out.println("✅ Syntax validation successful for: " + filename);
            System.out.println("   No syntax errors found. The file is ready for execution.");
            
        } catch (SyntaxException ex) {
            System.err.println("❌ Syntax validation failed:");
            System.err.println(ImpedimentFormatter.formatSyntaxImpediment(ex));
            System.exit(1);
        } catch (TokenException ex) {
            System.err.println("❌ Token validation failed:");
            System.err.println(ImpedimentFormatter.formatTokenImpediment(ex));
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("❌ Validation error: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (System.getProperty("scrum.debug") != null) {
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
        System.out.println("Copyright (c) 2023-2025 Jan Van Wassenhove");
        System.out.println("License: See LICENSE file in distribution");
    }
}
