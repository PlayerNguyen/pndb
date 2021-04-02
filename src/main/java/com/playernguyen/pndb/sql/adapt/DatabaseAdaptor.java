package com.playernguyen.pndb.sql.adapt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Adaptor using to generate, or prepare the statement to execute query.
 */
public interface DatabaseAdaptor {

    /**
     * Preparing a statement from connection.
     * Parameters will be iterate and set.
     *
     * @param sql a SQL query
     * @param objects objects which will be set
     * @return a generated {@link PreparedStatement}
     * @see Connection#prepareStatement(String)
     *
     */
    PreparedStatement prepareStatement(String sql, Object... objects) throws SQLException;

}
