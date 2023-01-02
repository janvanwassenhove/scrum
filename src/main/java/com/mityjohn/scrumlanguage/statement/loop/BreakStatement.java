package com.mityjohn.scrumlanguage.statement.loop;

import com.mityjohn.scrumlanguage.context.BreakContext;
import com.mityjohn.scrumlanguage.statement.Statement;

public class BreakStatement implements Statement {
    @Override
    public void execute() {
        BreakContext.getScope().invoke();
    }
}
