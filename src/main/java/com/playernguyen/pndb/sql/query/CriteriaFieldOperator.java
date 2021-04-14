package com.playernguyen.pndb.sql.query;

import org.jetbrains.annotations.NotNull;

public enum CriteriaFieldOperator {

    EQUAL("="),
    NOT_EQUAL("!="),

    MORE_THAN(">"),
    MORE_OR_EQUAL(">="),
    NOT_MORE_THAN("!>"),

    LESS_THAN("<"),
    LESS_OR_EQUAL("<="),
    NOT_LESS_THAN("!<"),
    LIKE(" LIKE ")
    ;

    private final String operator;

    CriteriaFieldOperator(String operator) {
        this.operator = operator;
    }

    @NotNull
    public String getOperator() {
        return operator;
    }
}
