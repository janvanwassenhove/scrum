package com.mityjohn.scrumlanguage.statement.loop;

import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.value.LogicalValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

@RequiredArgsConstructor
public class WhileLoopStatement extends AbstractLoopStatement {
    private final Expression hasNext;

    @Override
    protected void init() {
    }

    @Override
    protected boolean hasNext() {
        Value<?> value = hasNext.evaluate();
        return value instanceof LogicalValue && ((LogicalValue) value).getValue();
    }

    @Override
    protected void preIncrement() {
    }

    @Override
    protected void postIncrement() {
    }
}
