package com.epam.warehouse.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Parsed user request: {@code command [argument ...] [key=value ...]}.
 *
 * @param command   lower-case command name
 * @param arguments positional arguments (e.g. the target category)
 * @param options   {@code key=value} pairs in input order, keys are lower-case and unique
 */
public record Request(String command, List<String> arguments, Map<String, String> options) {

    public Request {
        Objects.requireNonNull(command, "Command must not be null");
        arguments = List.copyOf(arguments);
        options = Collections.unmodifiableMap(new LinkedHashMap<>(options));
    }

    public Optional<String> argument(int index) {
        return index < arguments.size() ? Optional.of(arguments.get(index)) : Optional.empty();
    }
}
