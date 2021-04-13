package com.playernguyen.pndb.sql.query;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilder {
    private final List<String> stringList = new ArrayList<>();

    public CriteriaBuilder() {
    }

    /**
     * Target to a specific field.
     *
     * @param field a targeted field.
     * @return a current criteria builder
     */
    public CriteriaBuilder newField(CriteriaField field) {
        stringList.add(String.format("%s %s ?", field.getFieldName(), field.getType().getOperator()));
        return this;
    }

    /**
     * New instance class of criteria builder.
     *
     * @return a new instance class of builder.
     */
    public static CriteriaBuilder newInstance() {
        return new CriteriaBuilder();
    }


    // ----------- AND LOGICAL ------------------- //

    /**
     * Append an AND into a building string.
     */
    private void and() {
        stringList.add(" AND ");
    }

    public CriteriaBuilder and(CriteriaField... fields) {
        for (int i = 0; i < fields.length; i++) {
            CriteriaField field = fields[i];
            this.newField(field);
            if (i + 1 < fields.length) {
                this.and();
            }
        }
        return this;
    }

    // ----------- OR LOGICAL ------------------- //
    public CriteriaBuilder or(CriteriaField... fields) {
        for (int i = 0; i < fields.length; i++) {
            CriteriaField field = fields[i];
            this.newField(field);
            if (i + 1 < fields.length) {
                this.or();
            }
        }
        return this;
    }

    private void or() {
        stringList.add(" OR ");
    }

    // ------- Builder -------- //

    public String build() {
        // generate new builder
        StringBuilder builder = new StringBuilder();
        // iterate all criteria and put it all
        for (String stringCriteria : stringList) {
            builder.append(stringCriteria);
        }
        // then, return
        return builder.toString();
    }

    @Override
    public String toString() {
        return build();
    }
}
