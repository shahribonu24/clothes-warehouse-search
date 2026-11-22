package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;
import com.epam.warehouse.controller.format.ProductFormatter;
import com.epam.warehouse.controller.parser.ParameterParser;
import com.epam.warehouse.controller.parser.SelectionParser;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.service.ProductService;
import com.epam.warehouse.service.Selection;
import com.epam.warehouse.service.ServiceException;

import java.util.List;
import java.util.Set;

public class FindCommand implements Command {

    private final ProductService service;

    public FindCommand(ProductService service) {
        this.service = service;
    }

    @Override
    public Response execute(Request request) throws ControllerException, ServiceException {
        Set<Category> categories = CommandSupport.target(request, true);
        List<Parameter<?>> parameters = ParameterParser.parse(request.options(), SelectionParser.KEYS);
        Selection selection = SelectionParser.parse(request.options());
        return Response.ok(ProductFormatter.format(service.find(categories, parameters, selection)));
    }
}
