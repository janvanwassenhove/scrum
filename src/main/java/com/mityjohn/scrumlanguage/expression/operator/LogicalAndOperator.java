package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.exception.ExecutionException;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.LogicalValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

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
            throw new ExecutionException(String.format("Unable to perform AND operator for non logical values `%s`, '%s'", left, right));
        }
    }
}
