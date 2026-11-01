package com.epam.warehouse.entity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Concrete product categories stored in the warehouse.
 */
public enum Category {
    JACKETS("jackets", Jacket.class),
    JEANS("jeans", Jeans.class),
    SNEAKERS("sneakers", Sneakers.class),
    BOOTS("boots", Boots.class);

    private final String key;
    private final Class<? extends Product<?>> type;

    Category(String key, Class<? extends Product<?>> type) {
        this.key = key;
        this.type = type;
    }

    public String key() {
        return key;
    }

    public Class<? extends Product<?>> type() {
        return type;
    }

    public static Optional<Category> byKey(String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = key.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(c -> c.key.equals(normalized)).findFirst();
    }

    public static Category of(Class<?> type) {
        return Arrays.stream(values())
                .filter(c -> c.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No category for type " + type.getSimpleName()));
    }

    /**
     * @return all categories whose products are instances of the given base type
     *         (e.g. {@code Clothing.class} → JACKETS, JEANS)
     */
    public static Set<Category> ofKind(Class<?> baseType) {
        return Arrays.stream(values())
                .filter(c -> baseType.isAssignableFrom(c.type))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Category.class)));
    }
}
