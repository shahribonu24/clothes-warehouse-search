package com.epam.warehouse.controller;

/**
 * Wrong user input detected by the controller (unknown command, bad parameter value, ...).
 */
public class ControllerException extends Exception {

    private static final long serialVersionUID = 1L;

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
