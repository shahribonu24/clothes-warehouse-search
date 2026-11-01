package com.epam.warehouse.dao.source;

import com.epam.warehouse.dao.DaoException;
import com.epam.warehouse.dao.mapper.RecordMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Reads products from a CSV file.
 * <p>
 * File format: the first meaningful line is a header with column names, values are separated
 * by commas, blank lines and lines starting with {@code #} are ignored.
 * The location is looked up in the file system first and then on the classpath.
 */
public class CsvSource<P> implements Source<P> {

    private static final String SEPARATOR = ",";
    private static final String COMMENT = "#";

    private final String location;
    private final RecordMapper<P> mapper;

    private BufferedReader reader;
    private List<String> header;
    private String nextLine;
    private int nextLineNumber;
    private int lineNumber;

    public CsvSource(String location, RecordMapper<P> mapper) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("CSV location must not be empty");
        }
        this.location = location;
        this.mapper = Objects.requireNonNull(mapper, "Mapper must not be null");
    }

    @Override
    public void init() throws DaoException {
        if (reader != null) {
            throw new IllegalStateException("Source " + location + " is already initialized");
        }
        reader = new BufferedReader(new InputStreamReader(open(), StandardCharsets.UTF_8));
        advance();
        if (nextLine == null) {
            throw new DaoException("File " + location + " is empty: header line expected");
        }
        header = Arrays.stream(split(nextLine)).map(h -> h.toLowerCase(Locale.ROOT)).toList();
        advance();
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public P next() throws DaoException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more records in " + location);
        }
        String line = nextLine;
        int current = nextLineNumber;
        advance();
        try {
            return mapper.map(CsvRecord.of(header, List.of(split(line))));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DaoException("Invalid data in " + location + " at line " + current + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Source<P> copy() {
        return new CsvSource<>(location, mapper);
    }

    @Override
    public String name() {
        return location;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    private InputStream open() throws DaoException {
        try {
            Path path = Path.of(location);
            if (Files.isRegularFile(path)) {
                return Files.newInputStream(path);
            }
        } catch (IOException | RuntimeException e) {
            throw new DaoException("Unable to open " + location + ": " + e.getMessage(), e);
        }
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
        if (resource == null) {
            throw new DaoException("Data file not found: " + location);
        }
        return resource;
    }

    private void advance() throws DaoException {
        try {
            String line;
            do {
                line = reader.readLine();
                lineNumber++;
            } while (line != null && (line.isBlank() || line.trim().startsWith(COMMENT)));
            nextLine = line;
            nextLineNumber = lineNumber;
        } catch (IOException e) {
            throw new DaoException("Unable to read " + location + ": " + e.getMessage(), e);
        }
    }

    private static String[] split(String line) {
        return Arrays.stream(line.split(SEPARATOR, -1)).map(String::trim).toArray(String[]::new);
    }
}
