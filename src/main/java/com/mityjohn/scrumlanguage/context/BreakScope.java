package com.mityjohn.scrumlanguage.context;

import lombok.Getter;

@Getter
public class BreakScope {
    private boolean invoked;

    public void invoke() {
        setInvoked(true);
    }

    private void setInvoked(boolean invoked) {
        this.invoked = invoked;
    }
}
