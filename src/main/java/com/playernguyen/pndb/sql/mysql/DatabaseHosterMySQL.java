package com.playernguyen.pndb.sql.mysql;

import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import com.playernguyen.pndb.sql.DatabaseOptions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A hoster with database connection, which contains {@link com.playernguyen.pndb.sql.DatabaseOptions}
 * to config your settings.
 */
public class DatabaseHosterMySQL implements DatabaseHoster {
    private final DatabaseOptions databaseOptions;
    private final HikariDataSource dataSource;

    public DatabaseHosterMySQL(DatabaseOptions options) {

        // Conflict option
        if (!(options instanceof DatabaseOptionsMySQL)) {
            throw new IllegalStateException("using DatabaseOptionsMySQL for MySQL hoster");
        }
        this.databaseOptions = options;

        // set to config
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url());
        hikariConfig.setUsername(this.databaseOptions.get(DatabaseOptionsMySQL.USERNAME));
        hikariConfig.setPassword(this.databaseOptions.get(DatabaseOptionsMySQL.PASSWORD));
        // hikariConfig.setMaximumPoolSize(8);

        // create new data source
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public DatabaseOptions getDatabaseOptions() {
        return databaseOptions;
    }

    private String url() {
        DatabaseOptionsMySQL databaseOptions = (DatabaseOptionsMySQL) getDatabaseOptions();
        return String.format(
                "jdbc:mysql://%s:%s/%s?%s",
                databaseOptions.getHost(),
                databaseOptions.getPort(),
                databaseOptions.getDatabase(),
                databaseOptions.getOptions()
        );
    }

    @Override
    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }
}
