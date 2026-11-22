package com.epam.warehouse.controller.format;

import com.epam.warehouse.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Shows products as a table: common attributes in columns, category-specific ones in "Details".
 */
public final class ProductFormatter {

    public static final String NOTHING_FOUND = "No products found matching your criteria.";

    private static final List<String> HEADER =
            List.of("ID", "Category", "Name", "Brand", "Color", "Gender", "Material", "Price", "Qty", "Details");

    private ProductFormatter() {
    }

    public static String format(List<? extends Product<?>> products) {
        if (products.isEmpty()) {
            return NOTHING_FOUND;
        }
        TableFormatter table = new TableFormatter(HEADER);
        for (Product<?> p : products) {
            table.addRow(List.of(
                    String.valueOf(p.getId()),
                    p.getCategory().key(),
                    p.getName(),
                    p.getBrand(),
                    p.getColor(),
                    String.valueOf(p.getGender()),
                    p.getMaterial(),
                    p.getPrice().toPlainString(),
                    String.valueOf(p.getQuantity()),
                    details(p)));
        }
        return "Found " + products.size() + " product(s):" + System.lineSeparator() + table.format();
    }

    static String details(Product<?> product) {
        return product.specificAttributes().entrySet().stream()
                .map(e -> e.getKey() + "=" + formatValue(e.getValue()))
                .collect(Collectors.joining(", "));
    }

    private static String formatValue(Object value) {
        if (value instanceof Double d && d == Math.rint(d)) {
            return String.valueOf(d.longValue());
        }
        return String.valueOf(value);
    }
}
