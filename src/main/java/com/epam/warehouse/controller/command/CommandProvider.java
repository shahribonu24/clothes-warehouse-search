package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.service.ProductService;

import java.util.EnumMap;
import java.util.Map;

/**
 * Maps command names to command objects.
 */
public class CommandProvider {

    private final Map<CommandName, Command> commands = new EnumMap<>(CommandName.class);

    public CommandProvider(ProductService service) {
        commands.put(CommandName.HELP, new HelpCommand());
        commands.put(CommandName.LIST, new ListCommand(service));
        commands.put(CommandName.FIND, new FindCommand(service));
        commands.put(CommandName.STATS, new StatsCommand(service));
        commands.put(CommandName.COST, new CostCommand(service));
        commands.put(CommandName.EXIT, new ExitCommand());
    }

    public Command get(String name) throws ControllerException {
        CommandName commandName = CommandName.byKey(name).orElseThrow(() -> new ControllerException(
                "Unknown command '" + name + "'. Type 'help' to see the list of commands"));
        return commands.get(commandName);
    }
}
