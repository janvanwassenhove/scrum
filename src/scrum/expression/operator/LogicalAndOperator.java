package scrum.expression.operator;

import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.Value;

public class LogicalAndOperator extends BinaryOperatorExpression {
    public LogicalAndOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        if (left instanceof LogicalValue && right instanceof LogicalValue) {
            return new LogicalValue(((LogicalValue) left).getValue() && ((LogicalValue) right).getValue());
        } else {
            throw new ExecutionException(String.format("Unable to perform AND operator for non logical values `%s`, '%s'", left, right));
        }
    }
}
