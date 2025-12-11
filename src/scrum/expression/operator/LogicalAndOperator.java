package scrum.expression.operator;

import scrum.expression.Expression;
import scrum.expression.value.LogicalValue;
import scrum.expression.value.TextValue;
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
            // Support string concatenation with AND operator
            return new TextValue(left.toString() + right.toString());
        }
    }
}
