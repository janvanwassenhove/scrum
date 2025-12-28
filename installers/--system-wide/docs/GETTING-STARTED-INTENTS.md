# Getting Started with #INTENT

The `#INTENT` construct allows you to write natural language descriptions that are automatically transformed into executable SCRUM code at compile time using LLMs.

## Quick Start

### Option 1: Cloud API (Recommended - No Installation)

Use a cloud LLM provider for instant #INTENT support:

**PowerShell:**
```powershell
# Using Groq (Free tier available)
$env:SCRUM_API_KEY = "your-groq-api-key"
$env:SCRUM_API_BASE_URL = "https://api.groq.com/openai/v1"
$env:SCRUM_API_MODEL = "llama-3.3-70b-versatile"
```

**Bash:**
```bash
# Using Groq (Free tier available)
export SCRUM_API_KEY="your-groq-api-key"
export SCRUM_API_BASE_URL="https://api.groq.com/openai/v1"
export SCRUM_API_MODEL="llama-3.3-70b-versatile"
```

Get a free API key from:
- **Groq**: https://console.groq.com/ (Fast, free tier)
- **OpenAI**: https://platform.openai.com/ (Most capable)
- **Cerebras**: https://cloud.cerebras.ai/ (Fast inference)
- **Together.ai**: https://api.together.xyz/ (Various models)

### Option 2: Local Ollama

For fully offline execution:

```bash
# Install Ollama
curl -fsSL https://ollama.com/install.sh | sh

# Pull a model
ollama pull llama3.2

# SCRUM will auto-detect running Ollama
```

## Your First #INTENT Program

Create `HelloIntent.scrum`:

```scrum
EPIC "HelloWorldApp"
    USER STORY "Greeting"
        #INTENT
        I want to create a simple greeting that displays "Hello from SCRUM!" to the user.
        #END INTENT
    END

    INSTANTIATE HelloWorldApp AS app
    app.Greeting
END
```

Run it:
```bash
cd development
java -jar scrum.jar examples/HelloIntent.scrum
```

## Example: Age Calculator with Intent

See `examples/IntentComputeBirthYear.scrum` for a complete example that:
1. Asks user for their age
2. Calculates birth year
3. Displays the result

```scrum
EPIC "BirthYearCalculator"
    USER STORY "ComputeBirthYear"
        #INTENT
        I want to ask the user for their current age.
        The input will be used to calculate and display their birth year 
        based on the current year 2024.
        #END INTENT
    END

    INSTANTIATE BirthYearCalculator AS calculator
    calculator.ComputeBirthYear
END
```

## How It Works

1. **Write Intent**: Describe what you want in natural language inside `#INTENT...#END INTENT`
2. **Compile-time Transformation**: SCRUM sends your intent to the LLM with examples of SCRUM syntax
3. **Code Generation**: LLM generates valid SCRUM code matching your intent
4. **Auto-validation**: Generated code is validated and re-parsed before execution
5. **Execution**: Your program runs with the generated implementation

## Auto-Detection

SCRUM automatically:
- Checks for API key → uses cloud API
- Falls back to Ollama if running locally
- Provides clear error messages if neither available

## Configuration Reference

See `docs/LLM-CONFIGURATION.md` for:
- Complete environment variable reference
- Provider-specific examples
- Cost considerations
- Troubleshooting guide

## Best Practices

1. **Be Specific**: Describe expected input/output clearly
2. **Break It Down**: Use multiple small intents rather than one large one
3. **Test Iteratively**: Start simple, add complexity gradually
4. **Review Generated Code**: Use verbose mode to see what LLM generated

## Next Steps

- Read `docs/LANGUAGE-REFERENCE.md` for full SCRUM syntax
- Review `examples/` directory for more #INTENT examples
- See `docs/API-DEFINITIONS.md` for creating APIs with intents
- Configure your preferred LLM provider in `docs/LLM-CONFIGURATION.md`
