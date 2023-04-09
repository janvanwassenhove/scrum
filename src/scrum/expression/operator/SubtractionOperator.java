package scrum.expression.operator;

import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.NumericValue;
import scrum.expression.value.TextValue;
import scrum.expression.value.Value;

import static scrum.expression.value.NullValue.NULL_INSTANCE;

public class SubtractionOperator extends BinaryOperatorExpression {
    public SubtractionOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        if (left == NULL_INSTANCE || right == NULL_INSTANCE) {
            throw new ExecutionException(String.format("Unable to perform subtraction for NULL values `%s`, '%s'", left, right));
        } else if (left instanceof NumericValue && right instanceof NumericValue) {
            return new NumericValue(((NumericValue) left).getValue() - ((NumericValue) right).getValue());
        } else {
            return new TextValue(left.toString().replaceAll(right.toString(), ""));
        }
    }
}

