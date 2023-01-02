package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.expression.AssignExpression;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.ArrayValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

public class ArrayValueOperator extends BinaryOperatorExpression implements AssignExpression {
    public ArrayValueOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        if (left instanceof ArrayValue) {
            Value<?> right = getRight().evaluate();
            return ((ArrayValue) left).getValue(((Double) right.getValue()).intValue());
        }
        return left;
    }

    @Override
    public void assign(Value<?> value) {
        Value<?> left = getLeft().evaluate();
        if (left instanceof ArrayValue) {
            Value<?> right = getRight().evaluate();
            ((ArrayValue) left).setValue(((Double) right.getValue()).intValue(), value);
        }
    }
}
