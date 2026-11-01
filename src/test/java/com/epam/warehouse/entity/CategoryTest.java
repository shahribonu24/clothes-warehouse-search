package com.epam.warehouse.entity;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {

    @Test
    void categoryIsFoundByKeyIgnoringCase() {
        assertEquals(Optional.of(Category.SNEAKERS), Category.byKey(" Sneakers "));
        assertEquals(Optional.empty(), Category.byKey("hats"));
        assertEquals(Optional.empty(), Category.byKey(null));
    }

    @Test
    void categoriesAreGroupedByBaseType() {
        assertEquals(EnumSet.of(Category.JACKETS, Category.JEANS), Category.ofKind(Clothing.class));
        assertEquals(EnumSet.of(Category.SNEAKERS, Category.BOOTS), Category.ofKind(Footwear.class));
        assertEquals(EnumSet.of(Category.JACKETS, Category.BOOTS), Category.ofKind(Waterproof.class));
        assertEquals(EnumSet.allOf(Category.class), Category.ofKind(Product.class));
    }

    @Test
    void categoryIsFoundByType() {
        assertEquals(Category.JEANS, Category.of(Jeans.class));
        assertThrows(IllegalArgumentException.class, () -> Category.of(String.class));
    }

    @Test
    void everyCategoryMatchesItsEntity() {
        assertEquals(Category.JACKETS, new Jacket().getCategory());
        assertEquals(Category.JEANS, new Jeans().getCategory());
        assertEquals(Category.SNEAKERS, new Sneakers().getCategory());
        assertEquals(Category.BOOTS, new Boots().getCategory());
    }
}
