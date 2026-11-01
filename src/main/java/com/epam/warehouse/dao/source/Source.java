package com.epam.warehouse.dao.source;

import com.epam.warehouse.dao.DaoException;

import java.io.Closeable;

/**
 * Sequential reader of products from some storage (CSV file, database, ...).
 * <p>
 * Lifecycle: {@link #copy()} → {@link #init()} → {@link #hasNext()} / {@link #next()} → {@link #close()}.
 * A source is stateful, so the DAO always works with a copy.
 */
public interface Source<P> extends Closeable {

    void init() throws DaoException;

    boolean hasNext();

    P next() throws DaoException;

    /**
     * @return a new, not yet initialized source reading the same data
     */
    Source<P> copy();

    /**
     * @return human-readable name of the source used in error messages
     */
    String name();
}
