package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;

public class ExitCommand implements Command {

    public static final String GOODBYE = "Goodbye!";

    @Override
    public Response execute(Request request) {
        return Response.exit(GOODBYE);
    }
}
