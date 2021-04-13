package com.playernguyen.pndb.sql.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderBuilder {

    private final List<OrderField> fieldList = new ArrayList<>();

    public static OrderBuilder newInstance() {
        return new OrderBuilder();
    }

    public OrderBuilder addField(OrderField... field) {
        this.fieldList.addAll(Arrays.asList(field));
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("ORDER BY");

        for (int i = 0; i < fieldList.size(); i++) {
            OrderField orderField = fieldList.get(i);
            builder.append(" ").append(orderField.getName()).append(" ").append(orderField.getFieldType().getAsQuery());
            if (i < fieldList.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public static OrderBuilder fromFields(OrderField... fields) {
        return new OrderBuilder().addField(fields);
    }

}
