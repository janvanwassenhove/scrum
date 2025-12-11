package scrum.statement;

import scrum.context.MemoryContext;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.NumericValue;
import scrum.expression.value.TextValue;
import scrum.expression.value.Value;
import scrum.token.TokenType;

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
