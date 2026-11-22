package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;
import com.epam.warehouse.service.ServiceException;

/**
 * One user command (Command pattern).
 */
public interface Command {

    Response execute(Request request) throws ControllerException, ServiceException;
}
