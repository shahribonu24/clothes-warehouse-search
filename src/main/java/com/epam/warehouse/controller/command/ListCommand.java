package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;
import com.epam.warehouse.controller.format.ProductFormatter;
import com.epam.warehouse.controller.parser.SelectionParser;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.service.ProductService;
import com.epam.warehouse.service.ServiceException;

import java.util.Set;

public class ListCommand implements Command {

    private final ProductService service;

    public ListCommand(ProductService service) {
        this.service = service;
    }

    @Override
    public Response execute(Request request) throws ControllerException, ServiceException {
        Set<Category> categories = CommandSupport.target(request, false);
        CommandSupport.requireNoOptions(request, SelectionParser.KEYS);
        return Response.ok(ProductFormatter.format(
                service.findAll(categories, SelectionParser.parse(request.options()))));
    }
}
