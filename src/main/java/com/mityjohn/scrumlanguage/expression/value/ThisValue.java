package com.mityjohn.scrumlanguage.expression.value;

import com.mityjohn.scrumlanguage.context.ClassInstanceContext;

public class ThisValue extends Value<ClassValue> {

    public static final ThisValue THIS_INSTANCE = new ThisValue();

    public ThisValue() {
        super(null);
    }

    @Override
    public ClassValue getValue() {
        return ClassInstanceContext.getValue();
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
