package com.playernguyen.pndb.sql;


import com.playernguyen.pndb.sql.query.DatabaseQueryBuilder;
import com.playernguyen.pndb.sql.sqlite.DatabaseHosterSQLite;
import com.playernguyen.pndb.sql.sqlite.DatabaseOptionsSQLite;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteTest {

    private static final String FILE_NAME = "database.db";
    // then create hoster
    DatabaseHosterSQLite hoster =
            new DatabaseHosterSQLite(new DatabaseOptionsSQLite(FILE_NAME));

    @Test
    public void createHosterWithSQLite() throws IOException, SQLException {
        hoster.connection();
    }


    @Test
    public void createTable() throws SQLException {
        DatabaseQueryBuilder.newInstance(hoster)
                .executeCustomUpdate("CREATE TABLE IF NOT EXISTS `database` (int INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        " value VARCHAR NOT NULL)");

    }

    @Test
    public void insertTable() throws SQLException {
        new DatabaseQueryBuilder(hoster)
                .insert("database")
                .values("value")
                .executeUpdate("love pizza #1");

    }

}
