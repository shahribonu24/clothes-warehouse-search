package com.epam.warehouse.controller.format;

import com.epam.warehouse.service.CategoryStatistics;
import com.epam.warehouse.service.InventoryStatistics;

import java.util.List;

public final class StatisticsFormatter {

    private static final List<String> HEADER = List.of("Category", "Lines", "Units", "Out of stock", "Stock value");

    private StatisticsFormatter() {
    }

    public static String format(InventoryStatistics statistics) {
        TableFormatter table = new TableFormatter(HEADER);
        for (CategoryStatistics s : statistics.categories()) {
            table.addRow(List.of(s.category().key(), String.valueOf(s.lines()), String.valueOf(s.units()),
                    String.valueOf(s.outOfStock()), s.value().toPlainString()));
        }
        table.addRow(List.of("TOTAL", String.valueOf(statistics.totalLines()),
                String.valueOf(statistics.totalUnits()), String.valueOf(statistics.totalOutOfStock()),
                statistics.totalValue().toPlainString()));
        return "Inventory statistics:" + System.lineSeparator() + table.format();
    }
}
