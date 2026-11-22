package com.epam.warehouse.controller.impl;

import com.epam.warehouse.controller.Controller;
import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;
import com.epam.warehouse.controller.command.Command;
import com.epam.warehouse.controller.command.CommandProvider;
import com.epam.warehouse.controller.parser.RequestParser;
import com.epam.warehouse.service.ProductService;
import com.epam.warehouse.service.ServiceException;

import java.util.Objects;

/**
 * Parses the request, finds the command (Command pattern) and turns every failure
 * into an error response, so the program keeps working after wrong input.
 */
public class ControllerImpl implements Controller {

    private final RequestParser parser = new RequestParser();
    private final CommandProvider provider;

    public ControllerImpl(ProductService service) {
        this.provider = new CommandProvider(Objects.requireNonNull(service, "Service must not be null"));
    }

    @Override
    public Response execute(String line) {
        try {
            Request request = parser.parse(line);
            Command command = provider.get(request.command());
            return command.execute(request);
        } catch (ControllerException e) {
            return Response.error(e.getMessage());
        } catch (ServiceException e) {
            return Response.error(e.getMessage());
        } catch (RuntimeException e) {
            // last line of defence: an unexpected failure must not stop the program
            return Response.error("Unexpected failure: " + e.getMessage());
        }
    }
}
