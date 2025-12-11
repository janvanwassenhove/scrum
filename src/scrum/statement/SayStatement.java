package scrum.statement;

import scrum.expression.Expression;
import scrum.expression.value.Value;

public record SayStatement(Expression expression) implements Statement {
    @Override
    public void execute() {
        Value<?> value = expression.evaluate();
        System.out.println(value);
    }
}
