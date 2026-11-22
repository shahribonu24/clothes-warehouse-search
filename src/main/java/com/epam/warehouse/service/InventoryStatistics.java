package com.epam.warehouse.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Statistics of several categories together with the totals.
 */
public record InventoryStatistics(List<CategoryStatistics> categories) {

    public InventoryStatistics {
        categories = List.copyOf(categories);
    }

    public int totalLines() {
        return categories.stream().mapToInt(CategoryStatistics::lines).sum();
    }

    public long totalUnits() {
        return categories.stream().mapToLong(CategoryStatistics::units).sum();
    }

    public BigDecimal totalValue() {
        return categories.stream().map(CategoryStatistics::value).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    public int totalOutOfStock() {
        return categories.stream().mapToInt(CategoryStatistics::outOfStock).sum();
    }
}
