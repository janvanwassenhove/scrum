package scrum.expression.operator;

import scrum.exception.ExecutionException;
import scrum.expression.Expression;
import scrum.expression.value.NumericValue;
import scrum.expression.value.Value;

public class ModuloOperator extends BinaryOperatorExpression {
    public ModuloOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        if (left instanceof NumericValue && right instanceof NumericValue) {
            return new NumericValue(((NumericValue) left).getValue() % ((NumericValue) right).getValue());
        } else {
            throw new ExecutionException(String.format("Unable to perform modulo for non numeric values `%s` and `%s`", left, right));
        }
    }
}
