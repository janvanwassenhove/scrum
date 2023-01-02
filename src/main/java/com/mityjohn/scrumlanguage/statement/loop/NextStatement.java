package com.mityjohn.scrumlanguage.statement.loop;

import com.mityjohn.scrumlanguage.context.NextContext;
import com.mityjohn.scrumlanguage.statement.Statement;

public class NextStatement implements Statement {
    @Override
    public void execute() {
        NextContext.getScope().invoke();
    }
}
