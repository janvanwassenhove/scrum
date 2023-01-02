package com.mityjohn.scrumlanguage.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.context.ReturnContext;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.Value;

@RequiredArgsConstructor
@Getter
public class ReturnStatement implements Statement {
    private final Expression expression;

    @Override
    public void execute() {
        Value<?> result = expression.evaluate();
        ReturnContext.getScope().invoke(result);
    }
}
