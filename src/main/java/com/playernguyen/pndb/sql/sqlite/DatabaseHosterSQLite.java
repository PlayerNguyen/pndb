package com.playernguyen.pndb.sql.sqlite;

import com.playernguyen.pndb.sql.DatabaseOptions;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * With file embedded database like SQLite, we do not need connection pooling.
 *
 */
public class DatabaseHosterSQLite implements DatabaseHoster {
    private final DatabaseOptions databaseOptions;

    public DatabaseHosterSQLite(DatabaseOptions databaseOptions) {
        this.databaseOptions = databaseOptions;
    }

    @Override
    public Connection connection() throws SQLException {
        String buildString = "jdbc:sqlite:" + databaseOptions.get(DatabaseOptionsSQLite.FILE_NAME);
        return DriverManager.getConnection(buildString);
    }
}
