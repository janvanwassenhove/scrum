package scrum.expression.operator;

import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.ComparableValue;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.Value;

import java.util.Objects;

import static scrum.expression.value.NullValue.NULL_INSTANCE;

public class LessThanOperator extends BinaryOperatorExpression {
    public LessThanOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        boolean result;
        if (left == NULL_INSTANCE || right == NULL_INSTANCE) {
            throw new ExecutionException(String.format("Unable to perform less than for NULL values `%s`, '%s'", left, right));
        } else if (Objects.equals(left.getClass(), right.getClass()) && left instanceof ComparableValue) {
            //noinspection unchecked,rawtypes
            result = ((Comparable) left.getValue()).compareTo(right.getValue()) < 0;
        } else {
            result = left.toString().compareTo(right.toString()) < 0;
        }
        return new LogicalValue(result);
    }
}

