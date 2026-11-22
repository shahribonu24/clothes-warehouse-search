package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.service.Selection;
import com.epam.warehouse.service.SortDirection;
import com.epam.warehouse.service.SortField;

import java.util.Map;
import java.util.Set;

/**
 * Reads the result-shaping options of a request:
 * <ul>
 *     <li>{@code sort=<field>} — sort field (default {@code id});</li>
 *     <li>{@code order=asc|desc} — sort direction (default {@code asc});</li>
 *     <li>{@code top=k} — only the first k products of the sorted list;</li>
 *     <li>{@code bottom=k} — only the last k products of the sorted list.</li>
 * </ul>
 */
public final class SelectionParser {

    public static final String SORT = "sort";
    public static final String ORDER = "order";
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    public static final Set<String> KEYS = Set.of(SORT, ORDER, TOP, BOTTOM);

    private SelectionParser() {
    }

    public static Selection parse(Map<String, String> options) throws ControllerException {
        SortField field = SortField.ID;
        if (options.containsKey(SORT)) {
            String value = options.get(SORT);
            field = SortField.byKey(value).orElseThrow(() -> new ControllerException(
                    "Unknown sort field '" + value + "'. Allowed: " + SortField.keys()));
        }
        SortDirection direction = SortDirection.ASC;
        if (options.containsKey(ORDER)) {
            String value = options.get(ORDER);
            direction = SortDirection.byKey(value).orElseThrow(() -> new ControllerException(
                    "Unknown order '" + value + "'. Allowed: asc, desc"));
        }
        Selection selection = Selection.sortedBy(field, direction);
        if (options.containsKey(TOP) && options.containsKey(BOTTOM)) {
            throw new ControllerException("Use either 'top' or 'bottom', not both");
        }
        if (options.containsKey(TOP)) {
            selection = selection.withLimit(limit(TOP, options.get(TOP)), Selection.Edge.TOP);
        } else if (options.containsKey(BOTTOM)) {
            selection = selection.withLimit(limit(BOTTOM, options.get(BOTTOM)), Selection.Edge.BOTTOM);
        }
        return selection;
    }

    private static int limit(String key, String value) throws ControllerException {
        try {
            return ValueParser.positiveInt(value);
        } catch (IllegalArgumentException e) {
            throw new ControllerException("Wrong value of '" + key + "': " + e.getMessage(), e);
        }
    }
}
