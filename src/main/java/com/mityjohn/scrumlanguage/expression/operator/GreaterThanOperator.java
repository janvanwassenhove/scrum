package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.exception.ExecutionException;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.ComparableValue;
import com.mityjohn.scrumlanguage.expression.value.LogicalValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

import java.util.Objects;

import static com.mityjohn.scrumlanguage.expression.value.NullValue.NULL_INSTANCE;

public class GreaterThanOperator extends BinaryOperatorExpression {
    public GreaterThanOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        boolean result;
        if (left == NULL_INSTANCE || right == NULL_INSTANCE) {
            throw new ExecutionException(String.format("Unable to perform greater than for NULL values `%s`, '%s'", left, right));
        } else if (Objects.equals(left.getClass(), right.getClass()) && left instanceof ComparableValue) {
            //noinspection unchecked,rawtypes
            result = ((Comparable) left.getValue()).compareTo(right.getValue()) > 0;
        } else {
            result = left.toString().compareTo(right.toString()) > 0;
        }
        return new LogicalValue(result);
    }
}

