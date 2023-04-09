package scrum.expression.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.expression.Expression;

@RequiredArgsConstructor
@Getter
public abstract class UnaryOperatorExpression implements OperatorExpression {
    private final Expression value;
}

