package com.playernguyen.pndb.sql.hoster;

import com.playernguyen.pndb.sql.DatabaseOptions;
import com.playernguyen.pndb.sql.mysql.DatabaseHosterMySQL;
import com.playernguyen.pndb.sql.mysql.DatabaseOptionsMySQL;

public class DatabaseHosterBuilder {
    private DatabaseOptions options;

    public DatabaseHosterBuilder() {
    }

    /**
     * Create new instance of builder class.
     * @return new builder class.
     */
    public static DatabaseHosterBuilder newInstance() {
        return new DatabaseHosterBuilder();
    }

    /**
     * Dispatch an option into builder.
     *
     * @param options an option class.
     * @return current builder class.
     */
    public DatabaseHosterBuilder options(DatabaseOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Build a hoster for MySQL.
     *
     * @return a hoster class.
     * @throws NullPointerException if an options was null (not set).
     * @throws IllegalStateException if options was not built for MySQL.
     * @see DatabaseOptions
     * @see DatabaseOptionsMySQL
     */
    public DatabaseHoster buildMySQL() {

        if (options == null) {
            throw new NullPointerException("options cannot be null");
        }

        // whether an option not for mysql
        if (!(options instanceof DatabaseOptionsMySQL)) {
            throw new IllegalStateException("require `options` for MySQL hoster");
        }

        return new DatabaseHosterMySQL(options);
    }

}
