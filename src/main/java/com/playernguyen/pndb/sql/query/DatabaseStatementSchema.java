package com.playernguyen.pndb.sql.query;

public enum DatabaseStatementSchema {

    SELECT("SELECT %select_columns% FROM %table% %criteria% %order%"),

    ;

    private final String template;

    DatabaseStatementSchema(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
