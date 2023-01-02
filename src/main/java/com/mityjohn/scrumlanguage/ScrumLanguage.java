package com.mityjohn.scrumlanguage;

import lombok.SneakyThrows;
import com.mityjohn.scrumlanguage.context.MemoryContext;
import com.mityjohn.scrumlanguage.context.definition.DefinitionContext;
import com.mityjohn.scrumlanguage.statement.CompositeStatement;
import com.mityjohn.scrumlanguage.token.Token;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScrumLanguage {

    @SneakyThrows
    public void execute(Path path) {
        String source = Files.readString(path);
        LexicalParser lexicalParser = new LexicalParser(source);
        List<Token> tokens = lexicalParser.parse();

        DefinitionContext.pushScope(DefinitionContext.newScope());
        MemoryContext.pushScope(MemoryContext.newScope());
        try {
            CompositeStatement statement = new CompositeStatement();
            StatementParser.parse(tokens, statement);
            statement.execute();
        } finally {
            DefinitionContext.endScope();
            MemoryContext.endScope();
        }
    }
}