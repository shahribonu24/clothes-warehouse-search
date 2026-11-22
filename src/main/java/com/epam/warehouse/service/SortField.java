package com.epam.warehouse.service;

import com.epam.warehouse.entity.Product;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Fields the products can be sorted by. Each field knows its own comparator (ascending order).
 */
public enum SortField {
    ID("id", Comparator.comparingLong(Product::getId)),
    NAME("name", Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER)),
    BRAND("brand", Comparator.comparing(Product::getBrand, String.CASE_INSENSITIVE_ORDER)),
    CATEGORY("category", Comparator.comparing(Product::getCategory)),
    PRICE("price", Comparator.comparing(Product::getPrice)),
    QUANTITY("quantity", Comparator.comparingInt(Product::getQuantity)),
    VALUE("value", Comparator.comparing(Product::getStockValue));

    private final String key;
    private final Comparator<Product<?>> comparator;

    SortField(String key, Comparator<Product<?>> comparator) {
        this.key = key;
        this.comparator = comparator;
    }

    public String key() {
        return key;
    }

    /**
     * @return ascending comparator of this field only
     */
    public Comparator<Product<?>> comparator() {
        return comparator;
    }

    public static Optional<SortField> byKey(String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = key.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(f -> f.key.equals(normalized)).findFirst();
    }

    public static String keys() {
        return Arrays.stream(values()).map(SortField::key).collect(Collectors.joining(", "));
    }
}
