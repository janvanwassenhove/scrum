package scrum.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.expression.value.ArrayValue;
import scrum.expression.value.Value;

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
