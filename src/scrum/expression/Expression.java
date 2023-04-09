package scrum.expression;

import scrum.expression.value.Value;

public interface Expression {
    Value<?> evaluate();
}
