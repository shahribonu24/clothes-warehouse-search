package com.epam.warehouse.service;

/**
 * Signals a failure of the service layer. Wraps DAO failures so that upper layers
 * never depend on the DAO layer directly.
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
