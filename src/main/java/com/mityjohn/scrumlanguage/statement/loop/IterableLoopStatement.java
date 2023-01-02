package com.mityjohn.scrumlanguage.statement.loop;

import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.context.MemoryContext;
import com.mityjohn.scrumlanguage.exception.ExecutionException;
import com.mityjohn.scrumlanguage.expression.Expression;
import com.mityjohn.scrumlanguage.expression.VariableExpression;
import com.mityjohn.scrumlanguage.expression.value.IterableValue;
import com.mityjohn.scrumlanguage.expression.value.Value;

import java.util.Iterator;

@RequiredArgsConstructor
public class IterableLoopStatement extends AbstractLoopStatement {
    private final VariableExpression variableExpression;
    private final Expression iterableExpression;

    private Iterator<Value<?>> iterator;

    @Override
    protected void init() {
        Value<?> value = iterableExpression.evaluate();
        if (!(value instanceof IterableValue))
            throw new ExecutionException(String.format("Unable to loop non IterableValue `%s`", value));
        this.iterator = ((IterableValue<?>) value).iterator();
    }

    @Override
    protected boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    protected void preIncrement() {
        MemoryContext.getScope().set(variableExpression.getName(), iterator.next());
    }

    @Override
    protected void postIncrement() {
    }
}
