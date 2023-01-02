package com.mityjohn.scrumlanguage.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.expression.value.ArrayValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ArrayExpression implements Expression {
    private final List<Expression> values;

    @Override
    public Value<?> evaluate() {
        return new ArrayValue(this);
    }
}
