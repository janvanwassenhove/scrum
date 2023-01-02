package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.LogicalValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

import java.util.Objects;

import static com.mityjohn.scrumlanguage.expression.value.NullValue.NULL_INSTANCE;

public class EqualsOperator extends BinaryOperatorExpression {
    public EqualsOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        boolean result;
        if (left == NULL_INSTANCE || right == NULL_INSTANCE) {
            result = left == right;
        } else if (Objects.equals(left.getClass(), right.getClass())) {
            result = left.getValue().equals(right.getValue());
        } else {
            result = left.toString().equals(right.toString());
        }
        return new LogicalValue(result);
    }
}
