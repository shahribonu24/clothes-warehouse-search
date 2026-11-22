package com.epam.warehouse.service;

import com.epam.warehouse.entity.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * How the found products are ordered and how many of them are returned.
 * <ul>
 *     <li>{@code field} / {@code direction} — sort order (default: by id, ascending);</li>
 *     <li>{@code limit} — keep only the first {@code limit} products of the sorted list
 *         ({@link Edge#TOP}) or the last ones ({@link Edge#BOTTOM}); {@code 0} means "no limit".</li>
 * </ul>
 * Example: the 3 most expensive products — {@code new Selection(PRICE, DESC, 3, TOP)}
 * (or, equivalently, {@code new Selection(PRICE, ASC, 3, BOTTOM)}).
 */
public record Selection(SortField field, SortDirection direction, int limit, Edge edge) {

    public enum Edge {
        TOP, BOTTOM
    }

    public static final Selection DEFAULT = new Selection(SortField.ID, SortDirection.ASC, 0, Edge.TOP);

    public Selection {
        Objects.requireNonNull(field, "Sort field must not be null");
        Objects.requireNonNull(direction, "Sort direction must not be null");
        Objects.requireNonNull(edge, "Edge must not be null");
        if (limit < 0) {
            throw new IllegalArgumentException("Limit can't be negative, but was " + limit);
        }
    }

    public static Selection sortedBy(SortField field, SortDirection direction) {
        return new Selection(field, direction, 0, Edge.TOP);
    }

    public Selection withLimit(int limit, Edge edge) {
        return new Selection(field, direction, limit, edge);
    }

    public boolean isLimited() {
        return limit > 0;
    }

    /**
     * @return comparator of the selected field in the selected direction; ties are always resolved
     *         by id in ascending order, so the result is predictable
     */
    public Comparator<Product<?>> comparator() {
        Comparator<Product<?>> byField = direction == SortDirection.ASC
                ? field.comparator()
                : field.comparator().reversed();
        return byField.thenComparing(SortField.ID.comparator());
    }

    /**
     * Sorts the products and applies the limit.
     *
     * @return a new list, the argument is not modified
     */
    public List<Product<?>> apply(List<Product<?>> products) {
        List<Product<?>> sorted = products.stream().sorted(comparator()).toList();
        if (!isLimited() || limit >= sorted.size()) {
            return sorted;
        }
        return edge == Edge.TOP
                ? sorted.subList(0, limit)
                : sorted.subList(sorted.size() - limit, sorted.size());
    }
}
