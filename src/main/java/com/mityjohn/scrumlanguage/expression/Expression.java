package com.mityjohn.scrumlanguage.expression;

import com.mityjohn.scrumlanguage.expression.value.Value;

public interface Expression {
    Value<?> evaluate();
}
