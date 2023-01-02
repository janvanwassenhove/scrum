package com.mityjohn.scrumlanguage.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.Value;

public record SayStatement(Expression expression) implements Statement {
    @Override
    public void execute() {
        Value<?> value = expression.evaluate();
        System.out.println(value);
    }
}
