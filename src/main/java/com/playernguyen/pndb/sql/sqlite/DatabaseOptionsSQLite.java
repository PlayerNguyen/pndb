package com.playernguyen.pndb.sql.sqlite;

import com.playernguyen.pndb.sql.DatabaseOptions;
import org.jetbrains.annotations.NotNull;

public class DatabaseOptionsSQLite extends DatabaseOptions {
    public static final String FILE_NAME = "filename";

    public DatabaseOptionsSQLite(@NotNull String filename) {
        this.put(FILE_NAME, filename);
    }

    public String getFileName() {
        return this.get(FILE_NAME);
    }
}
