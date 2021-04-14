package com.playernguyen.pndb.sql.task;

/**
 * Caller represents a callable class with one parameter.
 * @param <T>
 */
public interface Caller<T> {

    void call(T item) throws Exception;

}
