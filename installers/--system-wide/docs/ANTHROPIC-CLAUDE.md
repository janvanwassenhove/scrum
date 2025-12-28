# SCRUM Language with Anthropic Claude

This example demonstrates using SCRUM's `#INTENT` feature with Anthropic's Claude models.

## Setup

1. Get your Anthropic API key from: https://console.anthropic.com
2. Set environment variable:
   ```bash
   # Windows PowerShell
   $env:SCRUM_API_KEY="sk-ant-..."
   
   # Linux/Mac
   export SCRUM_API_KEY="sk-ant-..."
   ```

## Auto-Detection

SCRUM automatically detects Anthropic keys (starting with `sk-ant-`) and uses:
- **Default Base URL**: `https://api.anthropic.com`
- **Default Model**: `claude-sonnet-4-20250514` (Claude Sonnet 4)

## Available Models

Override the default model:
```bash
# Claude Sonnet 4 (default)
export SCRUM_API_MODEL="claude-sonnet-4-20250514"

# Claude Sonnet 4.5 (more advanced)
export SCRUM_API_MODEL="claude-sonnet-4.5-20250514"

# Claude Opus 4 (most capable, slower)
export SCRUM_API_MODEL="claude-opus-4-20250514"
```

## Example Usage

```scrum
EPIC "DataAnalyzer"
    USER STORY "AnalyzeNumbers"
        #INTENT
        I need to create a list of 5 numbers, calculate their sum and average,
        then display the results in a formatted way.
        #END INTENT
    END OF STORY
END OF EPIC

DataAnalyzer IS NEW DataAnalyzer
DataAnalyzer::AnalyzeNumbers USING []
```

Run it:
```bash
scrum examples/AnthropicExample.scrum
```

## Why Choose Claude?

- **Extended Context**: Handles longer, more complex intents
- **Reasoning**: Better at understanding nuanced requirements
- **Code Quality**: Generates clean, well-structured SCRUM code
- **Latest Models**: Claude Sonnet 4 and 4.5 are state-of-the-art

## Comparison with Other Providers

| Provider | Speed | Quality | Cost | Best For |
|----------|-------|---------|------|----------|
| **Anthropic Claude** | Medium | Excellent | Medium | Complex logic, reasoning |
| OpenAI GPT-4o | Fast | Excellent | Medium | General purpose |
| Groq | Very Fast | Good | Free tier | Simple intents, speed |
| Ollama | Variable | Good | Free | Local, offline development |
