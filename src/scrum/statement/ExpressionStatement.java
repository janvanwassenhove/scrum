package scrum.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.expression.Expression;

@RequiredArgsConstructor
@Getter
public class ExpressionStatement implements Statement {
    private final Expression expression;

    @Override
    public void execute() {
        expression.evaluate();
    }
}
