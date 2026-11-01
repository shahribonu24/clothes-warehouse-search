package com.epam.warehouse.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry of {@link ProductDao} implementations, one per product type.
 * <p>
 * Registration is type-safe ({@code register(Class<P>, ProductDao<P>)}), therefore the
 * unchecked cast in {@link #getInstance(Class)} can never fail.
 */
public final class DaoFactory {

    private static final Map<Class<?>, ProductDao<?>> REGISTRY = new ConcurrentHashMap<>();

    private DaoFactory() {
    }

    public static <P> void register(Class<P> type, ProductDao<P> dao) {
        if (type == null || dao == null) {
            throw new IllegalArgumentException("Type and DAO must not be null");
        }
        REGISTRY.put(type, dao);
    }

    public static void clear() {
        REGISTRY.clear();
    }

    @SuppressWarnings("unchecked")
    public static <P> ProductDao<P> getInstance(Class<P> type) throws DaoException {
        ProductDao<?> dao = REGISTRY.get(type);
        if (dao == null) {
            throw new DaoException("No data source is registered for " + type.getSimpleName());
        }
        return (ProductDao<P>) dao;
    }
}
