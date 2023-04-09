package scrum.expression.operator;

import scrum.expression.Expression;
import scrum.expression.value.Value;

public class ClassInstanceOperator extends UnaryOperatorExpression {
    public ClassInstanceOperator(Expression value) {
        super(value);
    }

    @Override
    public Value<?> evaluate() {
        return getValue().evaluate(); // will return toString() value
    }
}

