package com.mityjohn.scrumlanguage.expression.operator;

import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.Value;

public class ClassInstanceOperator extends UnaryOperatorExpression {
    public ClassInstanceOperator(Expression value) {
        super(value);
    }

    @Override
    public Value<?> evaluate() {
        return getValue().evaluate(); // will return toString() value
    }
}

