package com.playernguyen.pndb.sql.query;

import com.playernguyen.pndb.sql.adapt.DatabaseAdaptor;
import com.playernguyen.pndb.sql.adapt.DatabaseAdaptorBuilder;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DatabaseQueryBuilder {
    private DatabaseStatementSchema schema;
    /**
     * Statement is a unique string to order sql to execute a command. (irreplaceable and unique)
     */
    private String command;
    private String table;
    private String conditions;

    private final DatabaseAdaptor adaptor;

    public DatabaseQueryBuilder(DatabaseAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public DatabaseQueryBuilder(DatabaseHoster hoster) {
        this.adaptor = DatabaseAdaptorBuilder.newInstance().hoster(hoster).build();
    }

    public static DatabaseQueryBuilder newInstance(DatabaseAdaptor adaptor) {
        return new DatabaseQueryBuilder(adaptor);
    }

    public static DatabaseQueryBuilder newInstance(DatabaseHoster hoster) {
        return new DatabaseQueryBuilder(hoster);
    }

    /**
     * Set a target to interactively table.
     *
     * @param table a table to interact.
     * @return a current builder class.
     */
    public DatabaseQueryBuilder table(@NotNull String table) {
        this.table = table;
        return this;
    }

    /**
     * Set a custom command, a head (begin) order in SQL Statement.
     *
     * @param command a command to set
     * @return a current builder class.
     */
    public DatabaseQueryBuilder command(@NotNull String command) {
        this.command = command;
        return this;
    }

    /**
     * Build a select query, call this method will dispatch a select into statement.
     *
     * @param columns a column that you want to select.
     * @return a current builder class.
     * @throws IllegalStateException if a command was not null. Prevent a override command
     */
    public DatabaseQueryBuilder select(String... columns) {
        // if a statement was not null
        this.schemaIsNotEmptyValid();

        // build a new string list and process column data
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("SELECT ");

        // if no columns was detected, select all columns
        if (columns.length == 0) {
            statementBuilder.append("*");
        }

        // otherwise, iterate thru a list and append column into a list
        Iterator<String> iterator = Arrays.stream(columns).iterator();
        while (iterator.hasNext()) {
            statementBuilder.append(iterator.next());

            if (iterator.hasNext()) {
                statementBuilder.append(", ");
            }
        }
        // distinct
        statementBuilder.append(" FROM %table% ");

        // dispatch to the command variable
        this.command = statementBuilder.toString();
        return this;
    }

    public DatabaseQueryBuilder where(String criteria) {
        // if criteria was not null, dispatch into condition
        if (!criteria.equals("")) {
            this.conditions = " WHERE " + criteria;
        }
        return this;
    }

    public String build() {
        // if statement was null,
        if (command == null) {
            throw new NullPointerException("`statement` must not be null");
        }

        String builtString = command + " " + ((conditions != null) ? conditions : "");
        if (table == null) {
            throw new NullPointerException("table cannot be null");
        }
        return builtString.replaceAll("%table%", this.table);
    }

    public PreparedStatement prepare(Object... objects) throws SQLException {
        return this.adaptor.prepareStatement(build(), objects);
    }

    public ResultSet executeQuery(Object... objects) throws SQLException {
        return this.prepare(objects).executeQuery();
    }

    private void schemaIsNotEmptyValid() {
        if (this.schema != null) {
            throw new IllegalStateException("A schema variable cannot be override.");
        }
    }

    public static class CriteriaBuilder {
        private final List<String> stringList = new ArrayList<>();

        public CriteriaBuilder() {
        }

        public CriteriaBuilder of(String name) {
            stringList.add(name + " = ?");
            return this;
        }

        public CriteriaBuilder and() {
            stringList
                    .add(" AND ");
            return this;
        }

        public CriteriaBuilder or() {
            stringList
                    .add(" OR ");
            return this;
        }

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

}
