package com.epam.warehouse.service;

import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Product;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/**
 * Inventory figures of one category.
 *
 * @param lines      number of product lines (models)
 * @param units      number of units in stock
 * @param value      total stock value (sum of price × quantity)
 * @param outOfStock number of product lines with zero quantity
 */
public record CategoryStatistics(Category category, int lines, long units, BigDecimal value, int outOfStock) {

    public CategoryStatistics {
        Objects.requireNonNull(category, "Category must not be null");
        Objects.requireNonNull(value, "Value must not be null");
    }

    public static CategoryStatistics of(Category category, Collection<? extends Product<?>> products) {
        long units = 0;
        int outOfStock = 0;
        BigDecimal value = BigDecimal.ZERO.setScale(2);
        for (Product<?> product : products) {
            units += product.getQuantity();
            value = value.add(product.getStockValue());
            if (!product.isInStock()) {
                outOfStock++;
            }
        }
        return new CategoryStatistics(category, products.size(), units, value, outOfStock);
    }
}
