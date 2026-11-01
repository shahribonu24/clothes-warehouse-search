package com.epam.warehouse.dao;

/**
 * Signals a failure of the data access layer (missing or corrupted data source, I/O error).
 */
public class DaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
