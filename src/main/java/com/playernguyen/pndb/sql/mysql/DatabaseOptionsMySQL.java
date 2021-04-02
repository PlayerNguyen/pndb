package com.playernguyen.pndb.sql.mysql;

import com.playernguyen.pndb.sql.DatabaseOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseOptionsMySQL extends DatabaseOptions {
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DATABASE = "database";
    public static final String OPTIONS = "options";

    public DatabaseOptionsMySQL(String host,
                                String port,
                                String username,
                                String password,
                                String database,
                                String options) {
        this.put(HOST, host);
        this.put(PORT, port);
        this.put(USERNAME, username);
        this.put(PASSWORD, password);
        this.put(DATABASE, database);
        this.put(OPTIONS, options);
    }

    @Nullable
    public String getHost() {
        return this.get(HOST);
    }

    @Nullable
    public String getPort() {
        return this.get(PORT);
    }

    @Nullable
    public String getUsername() {
        return this.get(USERNAME);
    }

    @Nullable
    public String getPassword() {
        return this.get(PASSWORD);
    }

    @Nullable
    public String getDatabase() {
        return this.get(DATABASE);
    }

    @Nullable
    public String getOptions() {
        return this.get(OPTIONS);
    }

    public void setHost(@NotNull String value) {
        this.replace(HOST, value);
    }

    public void setPort(@NotNull String value) {
        this.replace(PORT, value);
    }

    public void setUsername(@NotNull String value) {
        this.replace(USERNAME, value);
    }

    public void setPassword(@NotNull String value) {
        this.replace(PASSWORD, value);
    }

    public void setDatabase(@NotNull String value) {
        this.replace(DATABASE, value);
    }

    public void setOptions(@NotNull String value) {
        this.replace(OPTIONS, value);
    }

}
