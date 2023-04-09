package scrum.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.context.ReturnContext;
import scrum.expression.Expression;
import scrum.expression.value.Value;

@RequiredArgsConstructor
@Getter
public class ReturnStatement implements Statement {
    private final Expression expression;

    @Override
    public void execute() {
        Value<?> result = expression.evaluate();
        ReturnContext.getScope().invoke(result);
    }
}
