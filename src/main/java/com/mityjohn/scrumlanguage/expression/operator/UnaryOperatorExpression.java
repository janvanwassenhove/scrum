package com.mityjohn.scrumlanguage.expression.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.expression.Expression;

@RequiredArgsConstructor
@Getter
public abstract class UnaryOperatorExpression implements OperatorExpression {
    private final Expression value;
}

