package scrum.expression.operator;

import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.NumericValue;
import scrum.expression.value.Value;

import static scrum.expression.value.NullValue.NULL_INSTANCE;

public class FloorDivisionOperator extends BinaryOperatorExpression {
    public FloorDivisionOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        if (left == NULL_INSTANCE || right == NULL_INSTANCE) {
            throw new ExecutionException(String.format("Unable to perform floor division for NULL values `%s`, '%s'", left, right));
        } else if (left instanceof NumericValue && right instanceof NumericValue) {
            return new NumericValue(Math.floor(((NumericValue) left).getValue() / ((NumericValue) right).getValue()));
        } else {
            throw new ExecutionException(String.format("Unable to divide non numeric values `%s` and `%s`", left, right));
        }
    }
}
