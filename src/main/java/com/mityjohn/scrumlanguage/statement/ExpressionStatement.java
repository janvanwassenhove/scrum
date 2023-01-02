package com.mityjohn.scrumlanguage.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.expression.Expression;

@RequiredArgsConstructor
@Getter
public class ExpressionStatement implements Statement {
    private final Expression expression;

    @Override
    public void execute() {
        expression.evaluate();
    }
}
