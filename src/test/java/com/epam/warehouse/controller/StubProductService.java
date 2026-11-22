package com.epam.warehouse.controller;

import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.service.InventoryStatistics;
import com.epam.warehouse.service.ProductService;
import com.epam.warehouse.service.Selection;
import com.epam.warehouse.service.ServiceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Fake service: remembers the arguments of the last call and returns prepared results,
 * so the controller can be tested without the service and DAO layers.
 */
class StubProductService implements ProductService {

    Set<Category> categories;
    List<Parameter<?>> parameters;
    Selection selection;
    List<Product<?>> result = List.of();
    InventoryStatistics statistics = new InventoryStatistics(List.of());
    ServiceException failure;

    @Override
    public List<Product<?>> find(Set<Category> categories, List<Parameter<?>> parameters, Selection selection)
            throws ServiceException {
        this.categories = categories;
        this.parameters = parameters;
        this.selection = selection;
        if (failure != null) {
            throw failure;
        }
        return result;
    }

    @Override
    public List<Product<?>> findAll(Set<Category> categories, Selection selection) throws ServiceException {
        return find(categories, List.of(), selection);
    }

    @Override
    public InventoryStatistics statistics(Set<Category> categories, List<Parameter<?>> parameters)
            throws ServiceException {
        this.categories = categories;
        this.parameters = parameters;
        if (failure != null) {
            throw failure;
        }
        return statistics;
    }

    @Override
    public BigDecimal cost(Set<Category> categories, List<Parameter<?>> parameters) throws ServiceException {
        return statistics(categories, parameters).totalValue();
    }
}
