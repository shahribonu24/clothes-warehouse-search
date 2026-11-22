package com.epam.warehouse.controller;

/**
 * Entry point of the controller layer: takes one text request from the view
 * (e.g. {@code find jackets size=M,L price=;150 sort=price order=desc})
 * and returns a ready-to-show response. Never throws on bad user input.
 */
public interface Controller {

    Response execute(String request);
}
