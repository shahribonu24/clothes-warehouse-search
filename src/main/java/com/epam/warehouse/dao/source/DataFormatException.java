package com.epam.warehouse.dao.source;

/**
 * A value in the data source can't be converted to the expected type.
 */
public class DataFormatException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public DataFormatException(String message) {
        super(message);
    }
}
