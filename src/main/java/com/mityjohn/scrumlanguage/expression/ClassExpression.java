package com.mityjohn.scrumlanguage.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.mityjohn.scrumlanguage.context.ClassInstanceContext;
import com.mityjohn.scrumlanguage.context.MemoryContext;
import com.mityjohn.scrumlanguage.context.MemoryScope;
import com.mityjohn.scrumlanguage.context.definition.ClassDefinition;
import com.mityjohn.scrumlanguage.context.definition.DefinitionContext;
import com.mityjohn.scrumlanguage.expression.value.ClassValue;
import com.mityjohn.scrumlanguage.expression.value.NullValue;
import com.mityjohn.scrumlanguage.expression.value.Value;
import com.mityjohn.scrumlanguage.statement.ClassStatement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public class ClassExpression implements Expression {
    private final String name;
    private final List<Expression> argumentExpressions;

    @Override
    public Value<?> evaluate() {
        //initialize class arguments
        List<Value<?>> values = argumentExpressions.stream().map(Expression::evaluate).collect(Collectors.toList());

        //get class's definition and statement
        ClassDefinition definition = DefinitionContext.getScope().getClass(name);
        ClassStatement classStatement = definition.getStatement();

        //set separate scope
        MemoryScope classScope = new MemoryScope(null);
        MemoryContext.pushScope(classScope);

        try {
            //initialize constructor arguments
            ClassValue classValue = new ClassValue(definition, classScope);
            ClassInstanceContext.pushValue(classValue);
            IntStream.range(0, definition.getArguments().size()).boxed()
                    .forEach(i -> MemoryContext.getScope()
                            .setLocal(definition.getArguments().get(i), values.size() > i ? values.get(i) : NullValue.NULL_INSTANCE));

            //execute function body
            DefinitionContext.pushScope(definition.getDefinitionScope());
            try {
                classStatement.execute();
            } finally {
                DefinitionContext.endScope();
            }

            return classValue;
        } finally {
            MemoryContext.endScope();
            ClassInstanceContext.popValue();
        }
    }
}
