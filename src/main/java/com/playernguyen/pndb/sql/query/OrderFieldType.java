package com.playernguyen.pndb.sql.query;

public enum OrderFieldType {

    ASC("ASC"),
    DESC("DESC");

    private final String asQuery;

    OrderFieldType(String asQuery) {
        this.asQuery = asQuery;
    }

    public String getAsQuery() {
        return asQuery;
    }

}
