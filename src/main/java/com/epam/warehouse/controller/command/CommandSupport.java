package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.parser.TargetParser;
import com.epam.warehouse.entity.Category;

import java.util.Set;

/**
 * Checks shared by the search commands.
 */
final class CommandSupport {

    private CommandSupport() {
    }

    /**
     * @return categories of the only allowed positional argument (all categories if it is absent)
     */
    static Set<Category> target(Request request, boolean required) throws ControllerException {
        if (request.arguments().size() > 1) {
            throw new ControllerException("Only one target is allowed, but got " + request.arguments()
                    + ". Allowed targets: " + TargetParser.allowedTargets());
        }
        if (required && request.arguments().isEmpty()) {
            throw new ControllerException("Target is missing. Usage: "
                    + CommandName.byKey(request.command()).map(CommandName::usage).orElse(request.command()));
        }
        return TargetParser.parse(request.argument(0).orElse(null));
    }

    static void requireNoOptions(Request request, Set<String> allowed) throws ControllerException {
        for (String key : request.options().keySet()) {
            if (!allowed.contains(key)) {
                throw new ControllerException("Parameter '" + key + "' is not supported by '" + request.command()
                        + "'. Usage: " + CommandName.byKey(request.command()).map(CommandName::usage).orElse(""));
            }
        }
    }
}
