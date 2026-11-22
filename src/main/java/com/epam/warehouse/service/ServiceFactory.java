package com.epam.warehouse.service;

import com.epam.warehouse.service.impl.ProductServiceImpl;

/**
 * Single access point to the service layer implementations.
 */
public final class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory();

    private final ProductService productService = new ProductServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public ProductService getProductService() {
        return productService;
    }
}
