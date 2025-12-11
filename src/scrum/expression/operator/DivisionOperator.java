package scrum.expression.operator;

import scrum.exception.ImpedimentCode;
import scrum.expression.Expression;
import scrum.expression.value.NumericValue;
import scrum.expression.value.Value;

public class DivisionOperator extends BinaryOperatorExpression {
    public DivisionOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value<?> evaluate() {
        Value<?> left = getLeft().evaluate();
        Value<?> right = getRight().evaluate();
        
        if (!(left instanceof NumericValue && right instanceof NumericValue)) {
            throw buildRuntimeException(
                String.format("Unable to divide non-numeric values `%s` and `%s`", left, right),
                ImpedimentCode.SCRUM_RUNTIME_TYPE_001,
                null
            );
        }
        
        try {
            double leftVal = ((NumericValue) left).getValue();
            double rightVal = ((NumericValue) right).getValue();
            
            if (rightVal == 0) {
                throw buildRuntimeException(
                    "Division by zero is not allowed",
                    ImpedimentCode.SCRUM_RUNTIME_ARITH_001,
                    null
                );
            }
            
            return new NumericValue(leftVal / rightVal);
        } catch (ArithmeticException ex) {
            throw buildRuntimeException(
                "Arithmetic error during division",
                ImpedimentCode.SCRUM_RUNTIME_ARITH_001,
                ex
            );
        }
    }
}
