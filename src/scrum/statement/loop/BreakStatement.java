package scrum.statement.loop;

import scrum.context.BreakContext;
import scrum.statement.Statement;

public class BreakStatement implements Statement {
    @Override
    public void execute() {
        BreakContext.getScope().invoke();
    }
}
