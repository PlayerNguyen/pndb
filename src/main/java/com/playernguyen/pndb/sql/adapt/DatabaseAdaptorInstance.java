package com.playernguyen.pndb.sql.adapt;

import com.playernguyen.pndb.sql.hoster.DatabaseHoster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAdaptorInstance implements DatabaseAdaptor {
    private final DatabaseHoster hoster;

    public DatabaseAdaptorInstance(DatabaseHoster hoster) {
        this.hoster = hoster;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql, Object... objects) throws SQLException {
        Connection connection = hoster.connection();
        // generate a statement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // dispatch parameters
        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setObject(i + 1, objects[i]);
        }
        // return
        return preparedStatement;
    }
}