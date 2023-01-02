package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.exception.ExecutionException;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.LogicalValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

public class NotOperator extends UnaryOperatorExpression {
    public NotOperator(Expression value) {
        super(value);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> value = getValue().evaluate();
        if (value instanceof LogicalValue) {
            return new LogicalValue(!(((LogicalValue) value).getValue()));
        } else {
            throw new ExecutionException(String.format("Unable to perform NOT operator for non logical value `%s`", value));
        }
    }
}

