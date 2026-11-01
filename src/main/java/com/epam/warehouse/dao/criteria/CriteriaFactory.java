package com.epam.warehouse.dao.criteria;

import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.Sneakers;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Creates an empty {@link SearchCriteria} for a product type.
 */
public final class CriteriaFactory {

    private static final Map<Class<?>, Supplier<SearchCriteria<?>>> SUPPLIERS = Map.of(
            Jacket.class, JacketSearchCriteria::new,
            Jeans.class, JeansSearchCriteria::new,
            Sneakers.class, SneakersSearchCriteria::new,
            Boots.class, BootsSearchCriteria::new
    );

    private CriteriaFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <P> SearchCriteria<P> forType(Class<P> type) {
        Supplier<SearchCriteria<?>> supplier = SUPPLIERS.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("No search criteria for " + type.getSimpleName());
        }
        // safe: every supplier creates criteria for exactly its key type
        return (SearchCriteria<P>) supplier.get();
    }
}
