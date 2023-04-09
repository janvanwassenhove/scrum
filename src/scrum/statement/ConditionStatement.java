package scrum.statement;

import lombok.Getter;
import scrum.context.MemoryContext;
import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.Value;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ConditionStatement implements Statement {
    private final Map<Expression, CompositeStatement> cases;

    public ConditionStatement() {
        //keep the cases order
        this.cases = new LinkedHashMap<>();
    }

    public void addCase(Expression caseCondition, CompositeStatement caseStatement) {
        cases.put(caseCondition, caseStatement);
    }

    @Override
    public void execute() {
        for (Map.Entry<Expression, CompositeStatement> entry : cases.entrySet()) {

            Expression condition = entry.getKey();
            Value<?> value = condition.evaluate();
            if (!(value instanceof LogicalValue)) {
                throw new ExecutionException(String.format("Cannot compare non logical value `%s`", value));
            }
            if (((LogicalValue) value).getValue()) {
                MemoryContext.pushScope(MemoryContext.newScope());
                try {
                    CompositeStatement statement = entry.getValue();
                    statement.execute();
                } finally {
                    MemoryContext.endScope();
                }
                break;
            }
        }
    }
}
