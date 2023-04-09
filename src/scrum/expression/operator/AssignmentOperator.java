package scrum.expression.operator;

import scrum.expression.AssignExpression;
import scrum.expression.Expression;
import scrum.expression.value.Value;

public class AssignmentOperator extends BinaryOperatorExpression {
    public AssignmentOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        if (getLeft() instanceof AssignExpression) {
            Value<?> right = getRight().evaluate();
            ((AssignExpression) getLeft()).assign(right);
            return getLeft().evaluate();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
