package com.epam.warehouse.dao;

import com.epam.warehouse.dao.criteria.SearchCriteria;

import java.util.List;

/**
 * Read access to products of one type.
 *
 * @param <P> product type
 */
public interface ProductDao<P> {

    /**
     * @return all products that satisfy every parameter of the criteria, in source order
     */
    List<P> find(SearchCriteria<P> criteria) throws DaoException;
}
