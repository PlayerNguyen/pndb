package com.playernguyen.pndb.sql.adapt;

import com.playernguyen.pndb.sql.hoster.DatabaseHoster;

/**
 * A builder for adaptor, using to build a things.
 */
public class DatabaseAdaptorBuilder {
    private DatabaseHoster hoster;

    /**
     * An empty constructor.
     */
    public DatabaseAdaptorBuilder() {
    }

    /**
     * Create new builder class.
     *
     * @return created builder class.
     */
    public static DatabaseAdaptorBuilder newInstance() {
        return new DatabaseAdaptorBuilder();
    }

    /**
     * Set hoster to this builder.
     *
     * @param hoster a dispatch hoster.
     * @return current builder class.
     */
    public DatabaseAdaptorBuilder hoster(DatabaseHoster hoster) {
        this.hoster = hoster;
        return this;
    }

    public DatabaseAdaptor build() {
        // hoster is null
        if (hoster == null) {
            throw new NullPointerException("Hoster was not set");
        }

        return new DatabaseAdaptorInstance(hoster);
    }
}
