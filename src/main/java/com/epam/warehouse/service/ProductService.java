package com.epam.warehouse.service;

import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Business operations over the warehouse inventory. Unlike the DAO, which works with
 * one product type, the service searches across several categories at once.
 * <p>
 * Parameters are given "untyped" (as created from user input). A parameter must be applicable
 * to at least one of the requested categories; categories it is not applicable to are skipped
 * (e.g. {@code waterproof=true} in "clothing" searches jackets only, because jeans can't be waterproof).
 */
public interface ProductService {

    /**
     * @param categories categories to search in (must not be empty)
     * @param parameters search parameters, an empty list matches every product
     * @param selection  sort order and limit of the result
     * @return matching products of all requested categories
     * @throws ServiceException if a parameter is not applicable to any requested category
     *                          or the data can't be read
     */
    List<Product<?>> find(Set<Category> categories, List<Parameter<?>> parameters, Selection selection)
            throws ServiceException;

    /**
     * Equivalent to {@code find(categories, List.of(), selection)}.
     */
    List<Product<?>> findAll(Set<Category> categories, Selection selection) throws ServiceException;

    /**
     * @return number of lines, units in stock, stock value and out-of-stock lines per category
     *         (only products matching the parameters are counted)
     */
    InventoryStatistics statistics(Set<Category> categories, List<Parameter<?>> parameters) throws ServiceException;

    /**
     * @return total cost (sum of price × quantity) of all products matching the parameters
     */
    BigDecimal cost(Set<Category> categories, List<Parameter<?>> parameters) throws ServiceException;
}
