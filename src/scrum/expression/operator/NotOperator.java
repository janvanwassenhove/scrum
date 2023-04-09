package scrum.expression.operator;

import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.Value;

public class NotOperator extends UnaryOperatorExpression {
    public NotOperator(Expression value) {
        super(value);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> value = getValue().evaluate();
        if (value instanceof LogicalValue) {
            return new LogicalValue(!(((LogicalValue) value).getValue()));
        } else {
            throw new ExecutionException(String.format("Unable to perform NOT operator for non logical value `%s`", value));
        }
    }
}

