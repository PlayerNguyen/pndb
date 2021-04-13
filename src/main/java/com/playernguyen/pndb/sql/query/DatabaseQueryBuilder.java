package com.playernguyen.pndb.sql.query;

import com.playernguyen.pndb.sql.adapt.DatabaseAdaptor;
import com.playernguyen.pndb.sql.adapt.DatabaseAdaptorBuilder;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseQueryBuilder {

    private DatabaseStatementSchema schema;
    /**
     * Statement is a unique string to order sql to execute a command. (irreplaceable and unique)
     */
    private final Map<String, String> replacementValue = new HashMap<>();

    private final DatabaseAdaptor adaptor;

    /**
     * Constructor with adaptor
     *
     * @param adaptor an adaptor to create a hook which connect to the database server
     */
    public DatabaseQueryBuilder(DatabaseAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    /**
     * Constructor with hoster parameter
     *
     * @param hoster a hoster parameter, use to get an adaptor by build.
     */
    public DatabaseQueryBuilder(DatabaseHoster hoster) {
        this.adaptor = DatabaseAdaptorBuilder.newInstance().hoster(hoster).build();
    }

    public static DatabaseQueryBuilder newInstance(DatabaseAdaptor adaptor) {
        return new DatabaseQueryBuilder(adaptor);
    }

    public static DatabaseQueryBuilder newInstance(DatabaseHoster hoster) {
        return new DatabaseQueryBuilder(hoster);
    }

    // ---------- SELECT -------------- //

    /**
     * Assign a select command for this builder.
     *
     * @param table   a table to select.
     * @param columns a column list to select, if empty is '*'.
     * @return the current query builder.
     */
    public DatabaseQueryBuilder select(@NotNull String table, String... columns) {
        // valid schema first
        this.schemaEmptyValid();

        // unless exist, set the schema
        this.schema = DatabaseStatementSchema.SELECT;

        // target the column
        StringBuilder columnSelection = new StringBuilder();
        if (columns.length == 0) {
            columnSelection.append("*");
        } else {
            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                columnSelection.append(column);

                if (i < column.length() - 1) {
                    columnSelection.append(", ");
                }
            }
        }
        this.replacementValue.put("%select_columns%", columnSelection.toString());
        this.replacementValue.put("%table%", table);

        return this;
    }

    /**
     * Assign to select with all columns in database.
     *
     * @param table a table to select.
     * @return the current query builder.
     */
    public DatabaseQueryBuilder selectAll(@NotNull String table) {
        return this.select(table, "*");
    }

    // ------------------ Criteria ------------------- //

    public DatabaseQueryBuilder criteria(@NotNull String rawCriteria) {
        this.replacementValue.put("%criteria%", "WHERE " + rawCriteria);
        return this;
    }

    public DatabaseQueryBuilder criteria(@NotNull CriteriaBuilder criteriaBuilder) {
        this.replacementValue.put("%criteria%", "WHERE " + criteriaBuilder.build());
        return this;
    }
    // ---------------------------------------------- //

    // ----------------- Order -----------------------//

    public DatabaseQueryBuilder order(@NotNull String rawOrder) {
        this.replacementValue.put("%order%", rawOrder);
        return this;
    }

    public DatabaseQueryBuilder order(@NotNull OrderBuilder orderBuilder) {
        return this.order(orderBuilder.build());
    }

    /**
     * Generate order by a field, this method will call {@link #order(String)}.
     * @param field an order field to generate.
     * @return this current database query builder class.
     */
    public DatabaseQueryBuilder order(@NotNull OrderField... field) {
        return this.order(OrderBuilder.fromFields(field).build());
    }

    // ---------------------------------------------- //

    /**
     * Check if the schema variable is empty or not. If the schema was not empty, throw {@link IllegalStateException}
     *
     * @throws IllegalStateException whether the schema was not empty
     */
    private void schemaEmptyValid() {
        if (schema != null) {
            throw new IllegalStateException("Overriding schema (another command).");
        }
    }

    /**
     * Generate a query.
     *
     * @return a generated query.
     */
    public String build() {
        // if schema not found
        if (schema == null) {
            throw new IllegalStateException("schema not found (no command)");
        }

        String templateBuilder = schema.getTemplate();
        for (Map.Entry<String, String> replacementEntry : replacementValue.entrySet()) {
            templateBuilder = templateBuilder.replaceAll(replacementEntry.getKey(), replacementEntry.getValue());
        }

        // criteria can be null
        if (!replacementValue.containsKey("%criteria%")) {
            templateBuilder = templateBuilder.replaceAll("%criteria%", "");
        }

        // order, too
        if (!replacementValue.containsKey("%order%")) {
            templateBuilder = templateBuilder.replaceAll("%order%", "");
        }
        return templateBuilder.trim();
    }

    /**
     * Prepare a {@link PreparedStatement} for execute by using adaptor.
     *
     * @param objects an object list to replace
     * @return a prepared statement
     * @throws SQLException an sql exception
     * @see java.sql.Connection#prepareStatement(String)
     */
    public PreparedStatement buildStatement(Object... objects) throws SQLException {
        return this.adaptor.prepareStatement(build(), objects);
    }

}
