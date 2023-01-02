package com.mityjohn.scrumlanguage.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.context.MemoryContext;
import com.mityjohn.scrumlanguage.expression.value.LogicalValue;
import com.mityjohn.scrumlanguage.expression.value.NumericValue;
import com.mityjohn.scrumlanguage.expression.value.TextValue;
import com.mityjohn.scrumlanguage.expression.value.Value;
import com.mityjohn.scrumlanguage.token.TokenType;

import java.util.function.Supplier;

public record InputStatement(String name, Supplier<String> consoleSupplier) implements Statement {
    @Override
    public void execute() {
        System.out.printf("Enter \"%s\" >>> ", name.replace("_", " "));
        String line = consoleSupplier.get();

        Value<?> value;
        if (line.matches(TokenType.Numeric.getRegex())) {
            value = new NumericValue(Double.parseDouble(line));
        } else if (line.matches(TokenType.Logical.getRegex())) {
            value = new LogicalValue(Boolean.valueOf(line));
        } else {
            value = new TextValue(line);
        }

        MemoryContext.getScope().set(name, value);
    }
}
