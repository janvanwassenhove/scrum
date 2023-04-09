package scrum.expression;

import scrum.expression.value.Value;

public interface AssignExpression {
    void assign(Value<?> value);
}
