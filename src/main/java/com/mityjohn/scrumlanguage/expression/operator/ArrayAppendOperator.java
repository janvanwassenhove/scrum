package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.ArrayValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

public class ArrayAppendOperator extends BinaryOperatorExpression {
    public ArrayAppendOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        if (left instanceof ArrayValue) {
            Value<?> right = getRight().evaluate();
            ((ArrayValue) left).appendValue(right);
        }
        return getLeft().evaluate();
    }
}
