package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.expression.AssignExpression;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.Value;

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
