package scrum.expression.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scrum.context.ExecutionContext;
import scrum.exception.ImpedimentCode;
import scrum.exception.ScrumRuntimeException;
import scrum.expression.Expression;

@RequiredArgsConstructor
@Getter
public abstract class BinaryOperatorExpression implements OperatorExpression {
    private final Expression left;
    private final Expression right;
    
    /**
     * Build a Scrum-style runtime exception with current execution context.
     */
    protected ScrumRuntimeException buildRuntimeException(String message, ImpedimentCode code, Throwable cause) {
        ExecutionContext.Context ctx = ExecutionContext.get();
        return ScrumRuntimeException.builder()
            .message(message)
            .impedimentCode(code)
            .snippet(this.toString())
            .epicName(ctx != null ? ctx.getEpicName() : null)
            .storyName(ctx != null ? ctx.getStoryName() : null)
            .fileName(ctx != null ? ctx.getFileName() : null)
            .cause(cause)
            .build();
    }
}

