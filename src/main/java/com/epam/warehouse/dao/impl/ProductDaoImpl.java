package com.epam.warehouse.dao.impl;

import com.epam.warehouse.dao.DaoException;
import com.epam.warehouse.dao.ProductDao;
import com.epam.warehouse.dao.criteria.SearchCriteria;
import com.epam.warehouse.dao.source.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDaoImpl<P> implements ProductDao<P> {

    private final Source<P> source;

    public ProductDaoImpl(Source<P> source) {
        this.source = Objects.requireNonNull(source, "Source must not be null");
    }

    @Override
    public List<P> find(SearchCriteria<P> criteria) throws DaoException {
        Objects.requireNonNull(criteria, "Criteria must not be null");
        // the source is stateful, so every search works with its own fresh copy
        try (Source<P> src = source.copy()) {
            src.init();
            List<P> products = new ArrayList<>();
            while (src.hasNext()) {
                P product = src.next();
                if (criteria.test(product)) {
                    products.add(product);
                }
            }
            return products;
        } catch (IOException e) {
            throw new DaoException("Unable to read " + source.name() + ": " + e.getMessage(), e);
        }
    }
}
