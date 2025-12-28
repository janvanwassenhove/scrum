# SCRUM Language Documentation

Welcome to the comprehensive documentation for the SCRUM Programming Language. This directory contains all the guides, references, and resources you need to master SCRUM development.

## Table of Contents

### 🚀 Getting Started

| Document | Description | Target Audience |
|----------|-------------|-----------------|
| **[SDK Installation Guide](SDK-INSTALLATION.md)** | Complete installation guide for all platforms | All users |
| **[Getting Started with Intents](GETTING-STARTED-INTENTS.md)** | Revolutionary AI-powered natural language programming | New users, AI enthusiasts |
| **[Quick Reference](SDK-QUICK-REFERENCE.md)** | Command cheat sheet and common patterns | All users |

### 📖 Language Reference

| Document | Description | Target Audience |
|----------|-------------|-----------------|
| **[Language Reference](LANGUAGE-REFERENCE.md)** | Complete SCRUM syntax, keywords, and semantics | Developers, complete reference |
| **[API Definitions](API-DEFINITIONS.md)** | REST API integration and endpoint definitions | API developers |

### 🤖 AI-Powered Programming

| Document | Description | Target Audience |
|----------|-------------|-----------------|
| **[LLM Configuration](LLM-CONFIGURATION.md)** | AI service setup and provider configuration | AI users |
| **[Fallback Chain Implementation](FALLBACK-CHAIN-IMPLEMENTATION.md)** | Multi-provider reliability and redundancy | Advanced AI users |
| **[Anthropic Claude Guide](ANTHROPIC-CLAUDE.md)** | Specific guidance for using Claude models | Claude users |

### 🔧 Technical & Development

| Document | Description | Target Audience |
|----------|-------------|-----------------|
| **[Release Process](RELEASE-PROCESS.md)** | Step-by-step guide for releasing new versions | Contributors, maintainers |
| **[SDK Implementation](SDK-IMPLEMENTATION.md)** | Technical architecture and implementation details | Contributors, advanced users |

---

## Quick Start Guide

### 1. **Install SCRUM SDK**
```bash
# Download from GitHub Releases and run installer
# Windows: installers\install.bat
# Linux/macOS: ./installers/install.sh
```
📚 [Complete Installation Guide →](SDK-INSTALLATION.md)

### 2. **Traditional SCRUM Programming**
```scrum
EPIC "HelloWorld"
    USER STORY "Greeting"
        SAY "Hello from SCRUM!"
    END
    
    INSTANTIATE HelloWorld AS app
    app.Greeting
END
```
📚 [Complete Language Reference →](LANGUAGE-REFERENCE.md)

### 3. **AI-Powered Programming with #INTENT**
```scrum
EPIC "Calculator"
    USER STORY "ComputeAge"
        #INTENT
        Ask the user for their birth year and calculate their current age.
        Display a friendly message with the result.
        #END INTENT
    END
END
```
📚 [Getting Started with #INTENT →](GETTING-STARTED-INTENTS.md)

### 4. **REST API Development**
```scrum
API "UserService"
    BASE PATH IS "/api/v1"
    
    I WANT TO DEFINE ENDPOINT "GetUser"
    METHOD IS GET
    PATH IS "/users/{id}"
    
    WHEN REQUEST
        #INTENT
        Retrieve user information by ID from a database.
        Return user details as JSON with name, email, and creation date.
        #END INTENT
    END
END
```
📚 [Complete API Reference →](API-DEFINITIONS.md)

---

## Documentation Categories

### **For New Users**
Start here if you're new to SCRUM:
1. [SDK Installation Guide](SDK-INSTALLATION.md) - Get SCRUM running
2. [Getting Started with Intents](GETTING-STARTED-INTENTS.md) - Experience AI programming
3. [Quick Reference](SDK-QUICK-REFERENCE.md) - Essential commands

### **For Developers**
Deep dive into SCRUM development:
1. [Language Reference](LANGUAGE-REFERENCE.md) - Complete syntax guide
2. [API Definitions](API-DEFINITIONS.md) - REST API development
3. [LLM Configuration](LLM-CONFIGURATION.md) - AI provider setup

### **For Contributors**
Contributing to the SCRUM language:
1. [Release Process](RELEASE-PROCESS.md) - How to release new versions
2. [SDK Implementation](SDK-IMPLEMENTATION.md) - Technical architecture
3. [Fallback Chain Implementation](FALLBACK-CHAIN-IMPLEMENTATION.md) - AI redundancy

### **For AI Enthusiasts**
Exploring AI-powered programming:
1. [Getting Started with Intents](GETTING-STARTED-INTENTS.md) - Natural language programming
2. [LLM Configuration](LLM-CONFIGURATION.md) - Provider options and setup
3. [Anthropic Claude Guide](ANTHROPIC-CLAUDE.md) - Claude-specific features

---

## Key Features Covered

### **Traditional Programming**
- 📝 Business-friendly syntax using Scrum terminology (EPIC, USER STORY)
- 🔧 Variables, operators, control flow with intuitive keywords
- 🌐 HTTP REST API definitions with declarative syntax
- 🚨 Comprehensive error handling with "Scrum Impediment" reporting

### **AI-Powered Programming**
- 🤖 **#INTENT blocks**: Write natural language that becomes executable code
- 🔗 **Multi-provider LLM support**: OpenAI, Anthropic, Groq, Ollama, and more
- ⚡ **Automatic fallback chains**: Seamlessly switch providers for reliability
- ✅ **Compile-time transformation**: AI code generation with validation

### **Development Experience**
- 🏗️ Complete SDK with global installation
- 🎨 IDE extensions for VS Code and IntelliJ IDEA
- 🔄 Automated release process with semantic versioning
- 📦 Cross-platform support (Windows, Linux, macOS)

---

## External Resources

- **GitHub Repository**: [https://github.com/janvanwassenhove/scrum](https://github.com/janvanwassenhove/scrum)
- **Releases & Downloads**: [https://github.com/janvanwassenhove/scrum/releases](https://github.com/janvanwassenhove/scrum/releases)
- **Issue Tracker**: [https://github.com/janvanwassenhove/scrum/issues](https://github.com/janvanwassenhove/scrum/issues)
- **Blog & Updates**: [mITy.John](https://www.mityjohn.com) 

---

**Need Help?**
- 🐛 **Found a bug?** Open an [issue on GitHub](https://github.com/janvanwassenhove/scrum/issues)
- 💡 **Have a feature idea?** Start a [discussion on GitHub](https://github.com/janvanwassenhove/scrum/discussions)
- 📧 **Need support?** Check the repository for contact information

---

**Copyright (c) 2023-2025 Jan Van Wassenhove**  
Licensed under terms specified in [LICENSE](../LICENSE) file.