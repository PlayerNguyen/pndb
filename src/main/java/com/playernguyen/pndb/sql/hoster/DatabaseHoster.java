package com.playernguyen.pndb.sql.hoster;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hoster, as the name, is a host - which open connection via client and database server.
 * Hoster is an interface, which will be implemented by sub-class such as (MySQL, SQLite, ...).
 */
public interface DatabaseHoster {

    /**
     * Open a connection by hikari data source.
     *
     * @return a generated connection.
     * @throws SQLException if something interrupt to generate a connection (more detail in {@link SQLException})
     * @see Connection
     */
    Connection connection() throws SQLException;

}
