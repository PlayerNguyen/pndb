package com.playernguyen.pndb.sql.query;

public class OrderField {
    private final String name;
    private final OrderFieldType fieldType;

    public OrderField(String name, OrderFieldType fieldType) {
        this.name = name;
        this.fieldType = fieldType;
    }

    public String getName() {
        return name;
    }

    public OrderFieldType getFieldType() {
        return fieldType;
    }

    public static OrderField createField(String name, OrderFieldType fieldType) {
        return new OrderField(name, fieldType);
    }


}
