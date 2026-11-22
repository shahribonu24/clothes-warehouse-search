package com.epam.warehouse.controller.format;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a plain-text table with columns as wide as their widest cell.
 */
public class TableFormatter {

    private static final String COLUMN_SEPARATOR = " | ";

    private final List<String> header;
    private final List<List<String>> rows = new ArrayList<>();

    public TableFormatter(List<String> header) {
        this.header = List.copyOf(header);
    }

    public TableFormatter addRow(List<String> row) {
        if (row.size() != header.size()) {
            throw new IllegalArgumentException("Row must have " + header.size() + " cells, but has " + row.size());
        }
        rows.add(List.copyOf(row));
        return this;
    }

    public String format() {
        int[] widths = new int[header.size()];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = header.get(i).length();
            for (List<String> row : rows) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }
        StringBuilder sb = new StringBuilder();
        appendRow(sb, header, widths);
        int total = (widths.length - 1) * COLUMN_SEPARATOR.length();
        for (int width : widths) {
            total += width;
        }
        sb.append("-".repeat(total)).append(System.lineSeparator());
        rows.forEach(row -> appendRow(sb, row, widths));
        return sb.toString().stripTrailing();
    }

    private static void appendRow(StringBuilder sb, List<String> cells, int[] widths) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < cells.size(); i++) {
            if (i > 0) {
                line.append(COLUMN_SEPARATOR);
            }
            line.append(String.format("%-" + widths[i] + "s", cells.get(i)));
        }
        sb.append(line.toString().stripTrailing()).append(System.lineSeparator());
    }
}
