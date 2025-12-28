# SCRUM Language - LLM Configuration Guide

The SCRUM language supports natural language intent blocks (`#INTENT`) that are processed by Large Language Models (LLMs) at compile time. Multiple LLM providers are supported with automatic fallback.

## Supported Providers

### 1. OpenAI (gpt-4o-mini, gpt-4o, o1, o3-mini)
Fast, reliable, and high-quality code generation. Supports latest GPT-4o and reasoning models.

```bash
export SCRUM_API_KEY="sk-..."
export SCRUM_API_BASE_URL="https://api.openai.com/v1"
export SCRUM_API_MODEL="gpt-4o-mini"  # or "gpt-4o", "o1", "o3-mini"
```

### 2. Anthropic Claude (Sonnet 4, Sonnet 4.5)
Excellent for complex reasoning and code generation with extended context.

```bash
export SCRUM_API_KEY="sk-ant-..."
export SCRUM_API_BASE_URL="https://api.anthropic.com"
export SCRUM_API_MODEL="claude-sonnet-4-20250514"  # or "claude-sonnet-4.5-20250514"
```

### 3. Groq (Ultra-fast inference)
Free tier available, extremely fast responses.

```bash
export SCRUM_API_KEY="gsk_..."
export SCRUM_API_BASE_URL="https://api.groq.com/openai/v1"
export SCRUM_API_MODEL="llama-3.3-70b-versatile"
```

### 4. Cerebras (Fast and affordable)
High performance at low cost.

```bash
export SCRUM_API_KEY="csk-..."
export SCRUM_API_BASE_URL="https://api.cerebras.ai/v1"
export SCRUM_API_MODEL="llama3.1-8b"
```

### 5. Together.ai (Multiple models)
Access to various open-source models.

```bash
export SCRUM_API_KEY="..."
export SCRUM_API_BASE_URL="https://api.together.xyz/v1"
export SCRUM_API_MODEL="meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo"
```

### 5. Ollama (Local, free)
Run models locally without API costs. Requires installation.

```bash
# Install Ollama from https://ollama.com
ollama pull llama3.2

# Configure (optional, these are defaults)
export SCRUM_OLLAMA_URL="http://localhost:11434"
export SCRUM_OLLAMA_MODEL="llama3.2"
```

## Automatic Provider Selection

By default, SCRUM uses `SCRUM_LLM_PROVIDER=auto` which:
1. Detects provider from API key prefix:
   - `sk-ant-` → Anthropic Claude
   - `sk-` or `sk-proj-` → OpenAI
   - `gsk_` → Groq
2. Falls back to local Ollama if no API key set

You can force a specific provider:

```bash
export SCRUM_LLM_PROVIDER="groq"    # Force Groq
export SCRUM_LLM_PROVIDER="ollama"  # Force Ollama
export SCRUM_LLM_PROVIDER="api"     # Force API provider
```

## Automatic Fallback Chain

SCRUM supports automatic fallback to alternative providers when:
- **Quota exceeded**: Provider has insufficient credits
- **Rate limiting**: Too many requests (HTTP 429)
- **Syntax failures**: LLM generates invalid code after retries

### Default Fallback Chain

The default chain is: `auto,groq,ollama`

1. **Auto**: Tries auto-detected provider (from API key)
2. **Groq**: Falls back to Groq (if `SCRUM_API_KEY` is Groq key)
3. **Ollama**: Falls back to local Ollama

### Custom Fallback Chain

Configure your own priority order:

```bash
# Try OpenAI first, then Groq, then Anthropic, finally Ollama
export SCRUM_LLM_FALLBACK_CHAIN="openai,groq,anthropic,ollama"

# Try only premium providers
export SCRUM_LLM_FALLBACK_CHAIN="anthropic,openai"

# Disable fallback (use only one provider)
export SCRUM_LLM_FALLBACK_CHAIN="openai"
```

**Supported provider names**: `auto`, `openai`, `anthropic`, `claude`, `groq`, `cerebras`, `together`, `ollama`

### How Fallback Works

When processing `#INTENT` blocks:

1. **Try first provider** in chain (up to 3 syntax retry attempts)
2. **On quota/rate-limit error** → skip to next provider
3. **On syntax errors after 3 attempts** → skip to next provider
4. **On network/timeout errors** → fail immediately (no fallback)
5. **Repeat** until success or all providers exhausted

Failed providers are cached during execution to avoid retrying them.

**Example scenario**:
- OpenAI quota exceeded → automatically tries Groq
- Groq rate limited → automatically tries Ollama
- All providers work together seamlessly

## Quick Start Examples

### Using Anthropic Claude (Recommended for complex reasoning)

1. Get API key from https://console.anthropic.com
2. Set environment variable:
   ```bash
   # Windows PowerShell
   $env:SCRUM_API_KEY="sk-ant-..."
   
   # Linux/Mac
   export SCRUM_API_KEY="sk-ant-..."
   ```
3. Run (uses Claude Sonnet 4 by default):
   ```bash
   scrum examples/IntentComputeBirthYear.scrum
   ```

### Using Groq (Recommended for speed - free and fast)

1. Get a free API key from https://console.groq.com
2. Set environment variable:
   ```bash
   # Windows PowerShell
   $env:SCRUM_API_KEY="gsk_..."
   $env:SCRUM_API_BASE_URL="https://api.groq.com/openai/v1"
   $env:SCRUM_API_MODEL="llama-3.3-70b-versatile"
   
   # Linux/Mac
   export SCRUM_API_KEY="gsk_..."
   export SCRUM_API_BASE_URL="https://api.groq.com/openai/v1"
   export SCRUM_API_MODEL="llama-3.3-70b-versatile"
   ```
3. Run your SCRUM program:
   ```bash
   scrum examples/IntentComputeBirthYear.scrum
   ```

### Using OpenAI

1. Get API key from https://platform.openai.com/api-keys
2. Set environment variable:
   ```bash
   # Windows PowerShell
   $env:SCRUM_API_KEY="sk-..."
   
   # Linux/Mac
   export SCRUM_API_KEY="sk-..."
   ```
3. Run (uses default OpenAI settings):
   ```bash
   scrum examples/IntentComputeBirthYear.scrum
   ```

### Using Local Ollama

1. Install Ollama: https://ollama.com/download
2. Pull model:
   ```bash
   ollama pull llama3.2
   ```
3. Run (Ollama auto-detected):
   ```bash
   scrum examples/IntentComputeBirthYear.scrum
   ```

## Environment Variables Reference

| Variable | Default | Description |
|----------|---------|-------------|
| `SCRUM_LLM_PROVIDER` | `auto` | Provider selection: `auto`, `api`, `openai`, `anthropic`, `claude`, `groq`, `cerebras`, `together`, `ollama` |
| `SCRUM_LLM_FALLBACK_CHAIN` | `auto,groq,ollama` | Comma-separated list of providers to try in order when quota/rate-limit errors occur |
| `SCRUM_API_KEY` | (none) | API key for cloud providers |
| `SCRUM_API_BASE_URL` | `https://api.openai.com/v1` | API endpoint URL |
| `SCRUM_API_MODEL` | `gpt-4o-mini` | Model name for API provider |
| `SCRUM_API_TEMPERATURE` | `0.3` | Temperature for code generation (0.0-1.0) |
| `SCRUM_OLLAMA_URL` | `http://localhost:11434` | Ollama API URL |
| `SCRUM_OLLAMA_MODEL` | `llama3.2` | Ollama model name |
| `SCRUM_OLLAMA_TEMPERATURE` | `0.3` | Temperature for Ollama |
| `SCRUM_LLM_TIMEOUT` | `120` | Timeout in seconds for LLM requests (applies to all providers) |

## Cost Considerations

When using the fallback chain, consider cost and availability:

- **Anthropic Claude**: Pay per token, Sonnet models are cost-effective for quality
- **Groq**: Free tier available, very fast (excellent fallback option)
- **Cerebras**: Low cost per token
- **OpenAI**: Pay per token, gpt-4o-mini is affordable, o1/o3 models cost more
- **Together.ai**: Pay per token, competitive pricing
- **Ollama**: Free, runs locally (requires GPU recommended)

**Recommended configurations**:
- **Budget-conscious**: `SCRUM_LLM_FALLBACK_CHAIN="groq,ollama"` (free options)
- **Quality-focused**: `SCRUM_LLM_FALLBACK_CHAIN="anthropic,openai,groq"` (premium first)
- **Speed-focused**: `SCRUM_LLM_FALLBACK_CHAIN="groq,cerebras"` (fastest services)

## Troubleshooting

### "No LLM provider available"
- Check if `SCRUM_API_KEY` is set (for cloud providers)
- Or check if Ollama 
- **Automatic fallback**: If `SCRUM_LLM_FALLBACK_CHAIN` is configured, SCRUM automatically tries the next provider
- **Manual solutions**: Wait a moment, upgrade plan, or configure fallback chain

### "Insufficient credits or quota"
- Provider has no remaining credits (HTTP 402 or quota error message)
- **Automatic fallback**: SCRUM automatically tries the next provider in the fallback chain
- **Manual solutions**: Add credits to account or configure fallback to free alternatives like Groq or Ollamaost:11434`

### "API request failed with status 401"
- Verify your API key is correct
- Check the API key has not expired

### "API request failed with status 429"
- Rate limit exceeded, wait a moment or upgrade plan
trying all providers"
- All providers in the fallback chain were tried and exhausted
- **Check which providers failed**: Look for "Failed providers:" in error message
- **Solutions**:
  - Add more providers to `SCRUM_LLM_FALLBACK_CHAIN`
  - Ensure at least one provider (like Ollama) is always available
  - Make your intent more specific and detailed
  - Review SCRUM syntax in `docs/LANGUAGE-REFERENCE.md`
  - Break complex logic into multiple simpler intent-3.3-70b-versatile)

### "Unable to translate intent to valid SCRUM code after 3 attempts"
- The LLM generated invalid SCRUM syntax multiple times
- Try making your intent more specific and detailed
- Review the SCRUM language syntax in `docs/LANGUAGE-REFERENCE.md`
- Consider breaking complex logic into multiple simpler intents
- Check the error message for specific syntax issues

### Slow responses
- Try Groq for fastest responses
- Use smaller models (e.g., `llama3.1-8b` instead of `llama-3.3-70b`)
- Check network connectivity

## Examples

See `development/examples/` for example programs using `#INTENT` blocks:
- `IntentComputeBirthYear.scrum` - User input and calculation
- `IntentBirthYearApi.scrum` - API endpoint definition
