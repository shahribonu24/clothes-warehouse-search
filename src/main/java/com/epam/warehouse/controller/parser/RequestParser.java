package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Splits a request line into a command, positional arguments and {@code key=value} options.
 * <p>
 * Tokens are separated by spaces; a value with spaces is written in double quotes:
 * {@code find sneakers name="Air Max"}. Keys and the command are case-insensitive.
 */
public class RequestParser {

    private static final char QUOTE = '"';
    private static final char ASSIGN = '=';

    public Request parse(String line) throws ControllerException {
        if (line == null || line.isBlank()) {
            throw new ControllerException("Empty request. Type 'help' to see the list of commands");
        }
        List<String> tokens = tokenize(line.trim());
        String command = tokens.get(0).toLowerCase(Locale.ROOT);
        if (command.indexOf(ASSIGN) >= 0) {
            throw new ControllerException("Request must start with a command, but was '" + tokens.get(0) + "'");
        }
        List<String> arguments = new ArrayList<>();
        Map<String, String> options = new LinkedHashMap<>();
        for (String token : tokens.subList(1, tokens.size())) {
            int assign = token.indexOf(ASSIGN);
            if (assign < 0) {
                if (!options.isEmpty()) {
                    throw new ControllerException("Unexpected '" + token
                            + "': arguments must be placed before key=value parameters");
                }
                arguments.add(token);
                continue;
            }
            String key = token.substring(0, assign).trim().toLowerCase(Locale.ROOT);
            String value = token.substring(assign + 1).trim();
            if (key.isEmpty()) {
                throw new ControllerException("Parameter name is missing in '" + token + "'");
            }
            if (value.isEmpty()) {
                throw new ControllerException("Value of parameter '" + key + "' is missing");
            }
            if (options.putIfAbsent(key, value) != null) {
                throw new ControllerException("Parameter '" + key + "' is given more than once. "
                        + "Use a comma to give several values: " + key + "=A,B");
            }
        }
        return new Request(command, arguments, options);
    }

    /**
     * Splits by whitespace outside of double quotes; quotes themselves are removed.
     */
    static List<String> tokenize(String line) throws ControllerException {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        boolean tokenStarted = false;
        for (char c : line.toCharArray()) {
            if (c == QUOTE) {
                quoted = !quoted;
                tokenStarted = true;
            } else if (Character.isWhitespace(c) && !quoted) {
                if (tokenStarted) {
                    tokens.add(current.toString());
                    current.setLength(0);
                    tokenStarted = false;
                }
            } else {
                current.append(c);
                tokenStarted = true;
            }
        }
        if (quoted) {
            throw new ControllerException("Closing quote (\") is missing");
        }
        if (tokenStarted) {
            tokens.add(current.toString());
        }
        return tokens;
    }
}
