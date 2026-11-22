package com.epam.warehouse.controller;

import com.epam.warehouse.controller.impl.ControllerImpl;
import com.epam.warehouse.service.ServiceFactory;

public final class ControllerFactory {

    private static final ControllerFactory INSTANCE = new ControllerFactory();

    private final Controller controller = new ControllerImpl(ServiceFactory.getInstance().getProductService());

    private ControllerFactory() {
    }

    public static ControllerFactory getInstance() {
        return INSTANCE;
    }

    public Controller getController() {
        return controller;
    }
}
