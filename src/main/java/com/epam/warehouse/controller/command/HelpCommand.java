package com.epam.warehouse.controller.command;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import com.epam.warehouse.controller.Response;
import com.epam.warehouse.controller.format.TableFormatter;
import com.epam.warehouse.controller.parser.ParameterType;
import com.epam.warehouse.controller.parser.TargetParser;
import com.epam.warehouse.service.SortField;

import java.util.List;

public class HelpCommand implements Command {

    private static final String NL = System.lineSeparator();

    @Override
    public Response execute(Request request) throws ControllerException {
        if (request.arguments().isEmpty()) {
            return Response.ok(commands());
        }
        String topic = request.arguments().get(0);
        CommandName name = CommandName.byKey(topic).orElseThrow(() -> new ControllerException(
                "Unknown command '" + topic + "'. Type 'help' to see the list of commands"));
        StringBuilder sb = new StringBuilder("Usage: ").append(name.usage()).append(NL)
                .append("  ").append(name.description());
        if (name == CommandName.FIND || name == CommandName.STATS || name == CommandName.COST) {
            sb.append(NL).append(NL).append(parameters());
        }
        if (name == CommandName.FIND || name == CommandName.LIST) {
            sb.append(NL).append(NL).append("Sort fields: ").append(SortField.keys())
                    .append(NL).append("top=k / bottom=k: first / last k products of the sorted list");
        }
        return Response.ok(sb.toString());
    }

    private static String commands() {
        StringBuilder sb = new StringBuilder("Commands:").append(NL);
        for (CommandName name : CommandName.values()) {
            sb.append("  ").append(name.usage()).append(NL).append("      ").append(name.description()).append(NL);
        }
        sb.append("Targets: ").append(TargetParser.allowedTargets()).append(NL)
                .append("Type 'help <command>' for details, e.g. 'help find'.");
        return sb.toString();
    }

    private static String parameters() {
        TableFormatter table = new TableFormatter(List.of("Parameter", "Format", "Categories"));
        for (ParameterType type : ParameterType.values()) {
            table.addRow(List.of(type.key(), type.format(), type.applicableCategories()));
        }
        return "Search parameters:" + NL + table.format();
    }
}
