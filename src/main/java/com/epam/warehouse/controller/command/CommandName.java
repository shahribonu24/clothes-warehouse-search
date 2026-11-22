package com.epam.warehouse.controller.command;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Names, syntax and short descriptions of all commands (used by the provider and by {@code help}).
 */
public enum CommandName {
    HELP("help [command]", "show the list of commands or details of one command"),
    LIST("list [target] [sort=<field>] [order=asc|desc] [top=k|bottom=k]",
            "show all products of the target, sorted"),
    FIND("find <target> [parameter=value ...] [sort=<field>] [order=asc|desc] [top=k|bottom=k]",
            "find products by any combination of parameters"),
    STATS("stats [target] [parameter=value ...]", "lines, units, out-of-stock lines and stock value per category"),
    COST("cost [target] [parameter=value ...]", "total cost (price x quantity) of the matching products"),
    EXIT("exit", "finish the program");

    private final String usage;
    private final String description;

    CommandName(String usage, String description) {
        this.usage = usage;
        this.description = description;
    }

    public String key() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String usage() {
        return usage;
    }

    public String description() {
        return description;
    }

    public static Optional<CommandName> byKey(String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = key.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(c -> c.key().equals(normalized)).findFirst();
    }
}
