package com.epam.warehouse.service.impl;

import com.epam.warehouse.dao.DaoException;
import com.epam.warehouse.dao.DaoFactory;
import com.epam.warehouse.dao.ProductDao;
import com.epam.warehouse.dao.criteria.CriteriaFactory;
import com.epam.warehouse.dao.criteria.SearchCriteria;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.service.CategoryStatistics;
import com.epam.warehouse.service.InventoryStatistics;
import com.epam.warehouse.service.ProductService;
import com.epam.warehouse.service.Selection;
import com.epam.warehouse.service.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product<?>> find(Set<Category> categories, List<Parameter<?>> parameters, Selection selection)
            throws ServiceException {
        Objects.requireNonNull(selection, "Selection must not be null");
        List<Product<?>> found = new ArrayList<>();
        searchByCategory(categories, parameters).values().forEach(found::addAll);
        return selection.apply(found);
    }

    @Override
    public List<Product<?>> findAll(Set<Category> categories, Selection selection) throws ServiceException {
        return find(categories, List.of(), selection);
    }

    @Override
    public InventoryStatistics statistics(Set<Category> categories, List<Parameter<?>> parameters)
            throws ServiceException {
        List<CategoryStatistics> result = new ArrayList<>();
        searchByCategory(categories, parameters)
                .forEach((category, products) -> result.add(CategoryStatistics.of(category, products)));
        return new InventoryStatistics(result);
    }

    @Override
    public BigDecimal cost(Set<Category> categories, List<Parameter<?>> parameters) throws ServiceException {
        return statistics(categories, parameters).totalValue();
    }

    /**
     * Searches every requested category. Categories where some parameter is not applicable
     * are not searched (they can't contain a matching product) but are present in the result
     * with an empty list, so statistics show them with zeros.
     */
    private Map<Category, List<? extends Product<?>>> searchByCategory(Set<Category> categories,
                                                                       List<Parameter<?>> parameters)
            throws ServiceException {
        validate(categories, parameters);
        Map<Category, List<? extends Product<?>>> result = new EnumMap<>(Category.class);
        for (Category category : EnumSet.copyOf(categories)) {
            boolean applicable = parameters.stream().allMatch(p -> p.isApplicableTo(category.type()));
            result.put(category, applicable ? search(category.type(), parameters) : List.of());
        }
        return result;
    }

    private static void validate(Set<Category> categories, List<Parameter<?>> parameters) throws ServiceException {
        if (categories == null || categories.isEmpty()) {
            throw new ServiceException("At least one category must be selected");
        }
        if (parameters == null || parameters.stream().anyMatch(Objects::isNull)) {
            throw new ServiceException("Search parameters must not be null");
        }
        for (Parameter<?> parameter : parameters) {
            boolean applicableSomewhere = categories.stream().anyMatch(c -> parameter.isApplicableTo(c.type()));
            if (!applicableSomewhere) {
                throw new ServiceException("Parameter '" + parameter.name() + "' is not applicable to "
                        + categories.stream().map(Category::key).sorted().collect(Collectors.joining(", ")));
            }
        }
    }

    private static <P extends Product<?>> List<P> search(Class<P> type, List<Parameter<?>> parameters)
            throws ServiceException {
        SearchCriteria<P> criteria = CriteriaFactory.forType(type);
        parameters.forEach(criteria::addChecked);
        try {
            ProductDao<P> dao = DaoFactory.getInstance(type);
            return dao.find(criteria);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
