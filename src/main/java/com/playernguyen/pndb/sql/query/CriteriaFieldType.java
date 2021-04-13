package com.playernguyen.pndb.sql.query;

import org.jetbrains.annotations.NotNull;

public enum CriteriaFieldType {

    EQUAL("="),
    NOT_EQUAL("!="),

    MORE_THAN(">"),
    MORE_OR_EQUAL(">="),
    NOT_MORE_THAN("!>"),

    LESS_THAN("<"),
    LESS_OR_EQUAL("<="),
    NOT_LESS_THAN("!<");

    private final String operator;

    CriteriaFieldType(String operator) {
        this.operator = operator;
    }

    @NotNull
    public String getOperator() {
        return operator;
    }
}
