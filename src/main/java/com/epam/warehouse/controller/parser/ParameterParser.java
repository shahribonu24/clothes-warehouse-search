package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.dao.criteria.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Converts {@code key=value} options of a request into search parameters.
 */
public final class ParameterParser {

    private ParameterParser() {
    }

    /**
     * @param options request options
     * @param ignored option keys that are not search parameters (e.g. {@code sort}, {@code top})
     */
    public static List<Parameter<?>> parse(Map<String, String> options, Set<String> ignored)
            throws ControllerException {
        List<Parameter<?>> parameters = new ArrayList<>();
        for (Map.Entry<String, String> option : options.entrySet()) {
            if (ignored.contains(option.getKey())) {
                continue;
            }
            ParameterType type = ParameterType.byKey(option.getKey())
                    .orElseThrow(() -> new ControllerException("Unknown parameter '" + option.getKey()
                            + "'. Type 'help find' to see all parameters"));
            try {
                parameters.add(type.create(option.getValue()));
            } catch (IllegalArgumentException e) {
                throw new ControllerException("Wrong value of '" + type.key() + "': " + e.getMessage(), e);
            }
        }
        return parameters;
    }
}
