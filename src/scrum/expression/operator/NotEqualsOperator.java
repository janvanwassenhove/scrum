package scrum.expression.operator;

import scrum.expression.Expression;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.Value;

import java.util.Objects;

import static scrum.expression.value.NullValue.NULL_INSTANCE;

public class NotEqualsOperator extends BinaryOperatorExpression {
    public NotEqualsOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        boolean result;
        if (left == NULL_INSTANCE || right == NULL_INSTANCE) {
            result = left != right;
        } else if (Objects.equals(left.getClass(), right.getClass())) {
            result = !left.getValue().equals(right.getValue());
        } else {
            result = !left.toString().equals(right.toString());
        }
        return new LogicalValue(result);
    }
}
