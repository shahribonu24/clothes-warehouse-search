package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;
import com.epam.warehouse.controller.parser.ParameterParser;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.service.InventoryStatistics;
import com.epam.warehouse.service.ProductService;
import com.epam.warehouse.service.ServiceException;

import java.util.List;
import java.util.Set;

/**
 * {@code cost [target] [parameter=value ...]} — total cost of the products matching the criteria,
 * e.g. {@code cost clothing season=WINTER}.
 */
public class CostCommand implements Command {

    private final ProductService service;

    public CostCommand(ProductService service) {
        this.service = service;
    }

    @Override
    public Response execute(Request request) throws ControllerException, ServiceException {
        Set<Category> categories = CommandSupport.target(request, false);
        List<Parameter<?>> parameters = ParameterParser.parse(request.options(), Set.of());
        InventoryStatistics statistics = service.statistics(categories, parameters);
        if (statistics.totalLines() == 0) {
            return Response.ok("No products found matching your criteria. Total cost: 0.00");
        }
        return Response.ok("Total cost: " + statistics.totalValue().toPlainString()
                + " (" + statistics.totalLines() + " product line(s), " + statistics.totalUnits() + " unit(s))");
    }
}
