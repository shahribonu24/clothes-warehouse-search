package com.epam.warehouse.controller;

import com.epam.warehouse.controller.impl.ControllerImpl;
import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.dao.criteria.parameter.BrandParameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.attribute.Gender;
import com.epam.warehouse.service.CategoryStatistics;
import com.epam.warehouse.service.InventoryStatistics;
import com.epam.warehouse.service.Selection;
import com.epam.warehouse.service.ServiceException;
import com.epam.warehouse.service.SortDirection;
import com.epam.warehouse.service.SortField;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Controller tested in isolation: the service is replaced by a stub.
 */
class ControllerUnitTest {

    private final StubProductService service = new StubProductService();
    private final Controller controller = new ControllerImpl(service);

    @Test
    void findPassesCategoriesParametersAndSelectionToService() {
        Response response = controller.execute("find footwear brand=Nike price=;100 sort=price order=desc top=2");
        assertEquals(Response.Status.OK, response.status());
        assertEquals(EnumSet.of(Category.SNEAKERS, Category.BOOTS), service.categories);
        assertEquals(List.of(new BrandParameter("Nike"), new PriceParameter(Range.atMost(new BigDecimal("100")))),
                service.parameters);
        assertEquals(new Selection(SortField.PRICE, SortDirection.DESC, 2, Selection.Edge.TOP), service.selection);
    }

    @Test
    void listWithoutTargetSearchesEverything() {
        controller.execute("list");
        assertEquals(EnumSet.allOf(Category.class), service.categories);
        assertEquals(List.of(), service.parameters);
        assertEquals(Selection.DEFAULT, service.selection);
    }

    @Test
    void foundProductsAreFormatted() {
        service.result = List.of(new Jacket().setId(7).setName("Parka").setBrand("Acme").setColor("Red")
                .setGender(Gender.MEN).setMaterial("Wool").setPrice(new BigDecimal("10")).setQuantity(1));
        Response response = controller.execute("find jackets");
        assertTrue(response.message().startsWith("Found 1 product(s):"));
        assertTrue(response.message().contains("Parka"));
        assertTrue(response.message().contains("10.00"));
    }

    @Test
    void emptyResultMessage() {
        assertEquals("No products found matching your criteria.", controller.execute("find boots").message());
    }

    @Test
    void serviceFailureBecomesErrorResponse() {
        service.failure = new ServiceException("Data file not found: boots.csv");
        Response response = controller.execute("find boots");
        assertEquals(Response.Status.ERROR, response.status());
        assertEquals("Error: Data file not found: boots.csv", response.message());
    }

    @Test
    void wrongInputDoesNotReachService() {
        assertTrue(controller.execute("find jackets price=abc").isError());
        assertTrue(controller.execute("find jackets size=M size=L").isError());
        assertTrue(controller.execute("find").isError());
        assertTrue(controller.execute("find jackets boots").isError());
        assertTrue(controller.execute("list jackets brand=Nike").isError());
        assertTrue(controller.execute("stats all sort=price").isError());
        assertNull(service.categories);
    }

    @Test
    void costUsesStatistics() {
        service.statistics = new InventoryStatistics(List.of(
                new CategoryStatistics(Category.JACKETS, 2, 8, new BigDecimal("1594.97"), 0)));
        Response response = controller.execute("cost clothing season=WINTER");
        assertEquals("Total cost: 1594.97 (2 product line(s), 8 unit(s))", response.message());
        assertEquals(EnumSet.of(Category.JACKETS, Category.JEANS), service.categories);
    }

    @Test
    void unexpectedRuntimeFailureIsCaught() {
        Controller failing = new ControllerImpl(new StubProductService() {
            @Override
            public InventoryStatistics statistics(java.util.Set<Category> c,
                                                  java.util.List<com.epam.warehouse.dao.criteria.parameter.Parameter<?>> p) {
                throw new IllegalStateException("boom");
            }
        });
        Response response = failing.execute("stats");
        assertEquals(Response.Status.ERROR, response.status());
        assertTrue(response.message().contains("boom"));
    }

    @Test
    void factoryProvidesController() {
        assertTrue(ControllerFactory.getInstance().getController() instanceof ControllerImpl);
    }
}
