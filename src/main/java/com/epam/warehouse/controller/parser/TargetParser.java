package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Clothing;
import com.epam.warehouse.entity.Footwear;
import com.epam.warehouse.entity.Product;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts the target of a request into a set of categories:
 * a single category ({@code jackets}) or a group ({@code all}, {@code clothing}, {@code footwear}).
 */
public final class TargetParser {

    public static final String ALL = "all";
    public static final String CLOTHING = "clothing";
    public static final String FOOTWEAR = "footwear";

    private TargetParser() {
    }

    public static Set<Category> parse(String target) throws ControllerException {
        if (target == null || target.isBlank()) {
            return Category.ofKind(Product.class);
        }
        String key = target.trim().toLowerCase(Locale.ROOT);
        switch (key) {
            case ALL:
                return Category.ofKind(Product.class);
            case CLOTHING:
                return Category.ofKind(Clothing.class);
            case FOOTWEAR:
                return Category.ofKind(Footwear.class);
            default:
                return Category.byKey(key)
                        .map(EnumSet::of)
                        .orElseThrow(() -> new ControllerException("Unknown category '" + target.trim()
                                + "'. Allowed: " + allowedTargets()));
        }
    }

    public static String allowedTargets() {
        return ALL + ", " + CLOTHING + ", " + FOOTWEAR + ", "
                + Arrays.stream(Category.values()).map(Category::key).collect(Collectors.joining(", "));
    }
}
