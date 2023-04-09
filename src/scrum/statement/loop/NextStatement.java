package scrum.statement.loop;

import scrum.context.NextContext;
import scrum.statement.Statement;

public class NextStatement implements Statement {
    @Override
    public void execute() {
        NextContext.getScope().invoke();
    }
}
