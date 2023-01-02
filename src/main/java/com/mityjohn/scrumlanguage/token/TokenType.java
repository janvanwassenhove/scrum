package com.mityjohn.scrumlanguage.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {
    Comment("\\#REVIEW.*|\\#SPRINTGOAL.*"),
    LineBreak("[\\n\\r]"),
    Whitespace("[\\s\\t]"),
    Keyword("(EPIC|END OF EPIC|USER STORY|END OF STORY|ASK|SAY|IF|ELSE|ELSEIF|END IF|END OF ITERATION|end|scenario|RETURN ANSWER|I WANT TO ITERATE|FOR RANGE|by|break|next)(?=\\s|$)"),
    GroupDivider("(USING \\[|TILL|\\]|\\,|\\{|}|[.]{2})"),
    Logical("(true|false)(?=\\s|$)"),
    Numeric("([-]?(?=[.]?[0-9])[0-9]*(?![.]{2})[.]?[0-9]*)"),
    Null("(null)(?=,|\\s|$)"),
    This("(this)(?=,|\\s|$)"),
    Text("\"([^\"]*)\""),
    Operator("(\\+|-|\\*|/{1,2}|%|>=|>|<=|<{1,2}|={1,2}|IS{1,2}|!=|!|:{2}|\\(|\\)|(NEW|AND|OR)(?=\\s|$))"),
    Variable("[a-zA-Z_]+[a-zA-Z0-9_]*");

    private final String regex;
}

