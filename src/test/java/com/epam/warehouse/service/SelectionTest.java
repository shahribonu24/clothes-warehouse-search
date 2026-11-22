package com.epam.warehouse.service;

import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SelectionTest {

    private static Product<?> jacket(long id, String name, String price, int quantity) {
        return new Jacket().setId(id).setName(name).setBrand("B").setPrice(new BigDecimal(price)).setQuantity(quantity);
    }

    private static final List<Product<?>> PRODUCTS = List.of(
            jacket(3, "beta", "30", 1),
            jacket(1, "Alpha", "50", 5),
            jacket(2, "gamma", "10", 0),
            jacket(4, "delta", "30", 2));

    private static List<Long> ids(List<Product<?>> products) {
        return products.stream().map(Product::getId).toList();
    }

    @Test
    void defaultSelectionSortsById() {
        assertEquals(List.of(1L, 2L, 3L, 4L), ids(Selection.DEFAULT.apply(PRODUCTS)));
        assertFalse(Selection.DEFAULT.isLimited());
    }

    @Test
    void sortsByPriceAscendingWithTiesResolvedById() {
        Selection selection = Selection.sortedBy(SortField.PRICE, SortDirection.ASC);
        assertEquals(List.of(2L, 3L, 4L, 1L), ids(selection.apply(PRODUCTS)));
    }

    @Test
    void sortsDescending() {
        Selection selection = Selection.sortedBy(SortField.QUANTITY, SortDirection.DESC);
        assertEquals(List.of(1L, 4L, 3L, 2L), ids(selection.apply(PRODUCTS)));
    }

    @Test
    void sortsNamesCaseInsensitively() {
        Selection selection = Selection.sortedBy(SortField.NAME, SortDirection.ASC);
        assertEquals(List.of(1L, 3L, 4L, 2L), ids(selection.apply(PRODUCTS)));
    }

    @Test
    void sortsByStockValue() {
        // values: id1=250, id3=30, id4=60, id2=0
        Selection selection = Selection.sortedBy(SortField.VALUE, SortDirection.DESC);
        assertEquals(List.of(1L, 4L, 3L, 2L), ids(selection.apply(PRODUCTS)));
    }

    @Test
    void topKeepsFirstItems() {
        Selection selection = Selection.sortedBy(SortField.PRICE, SortDirection.DESC).withLimit(2, Selection.Edge.TOP);
        assertTrue(selection.isLimited());
        assertEquals(List.of(1L, 3L), ids(selection.apply(PRODUCTS)));
    }

    @Test
    void bottomKeepsLastItems() {
        Selection selection = Selection.sortedBy(SortField.PRICE, SortDirection.DESC)
                .withLimit(1, Selection.Edge.BOTTOM);
        assertEquals(List.of(2L), ids(selection.apply(PRODUCTS)));
    }

    @Test
    void limitGreaterThanSizeReturnsEverything() {
        Selection selection = Selection.DEFAULT.withLimit(10, Selection.Edge.BOTTOM);
        assertEquals(4, selection.apply(PRODUCTS).size());
    }

    @Test
    void applyDoesNotModifyArgument() {
        List<Product<?>> source = new java.util.ArrayList<>(PRODUCTS);
        Selection.sortedBy(SortField.PRICE, SortDirection.ASC).apply(source);
        assertEquals(PRODUCTS, source);
    }

    @Test
    void negativeLimitIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Selection.DEFAULT.withLimit(-1, Selection.Edge.TOP));
        assertThrows(NullPointerException.class, () -> new Selection(null, SortDirection.ASC, 0, Selection.Edge.TOP));
    }

    @Test
    void sortFieldAndDirectionAreFoundByKey() {
        assertEquals(SortField.PRICE, SortField.byKey(" Price ").orElseThrow());
        assertTrue(SortField.byKey("weight").isEmpty());
        assertEquals(SortDirection.DESC, SortDirection.byKey("desc").orElseThrow());
        assertTrue(SortDirection.byKey("up").isEmpty());
    }
}
