package com.epam.warehouse.entity;

import com.epam.warehouse.entity.attribute.Fit;

import java.util.Map;
import java.util.Objects;

public class Jeans extends Clothing<Jeans> {

    private static final long serialVersionUID = 1L;
    private static final int MIN_INCHES = 20;
    private static final int MAX_INCHES = 50;

    private int waist;
    private int length;
    private Fit fit;

    public Jeans() {
    }

    @Override
    public Category getCategory() {
        return Category.JEANS;
    }

    public int getWaist() {
        return waist;
    }

    public Jeans setWaist(int waist) {
        this.waist = requireInches(waist, "Waist");
        return this;
    }

    public int getLength() {
        return length;
    }

    public Jeans setLength(int length) {
        this.length = requireInches(length, "Length");
        return this;
    }

    public Fit getFit() {
        return fit;
    }

    public Jeans setFit(Fit fit) {
        this.fit = Objects.requireNonNull(fit, "Fit must not be null");
        return this;
    }

    private static int requireInches(int value, String fieldName) {
        if (value < MIN_INCHES || value > MAX_INCHES) {
            throw new IllegalArgumentException(fieldName + " must be between " + MIN_INCHES + " and "
                    + MAX_INCHES + " inches, but was " + value);
        }
        return value;
    }

    @Override
    public Map<String, Object> specificAttributes() {
        Map<String, Object> attributes = super.specificAttributes();
        attributes.put("waist", waist);
        attributes.put("length", length);
        attributes.put("fit", fit);
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Jeans other = (Jeans) o;
        return waist == other.waist && length == other.length && fit == other.fit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), waist, length, fit);
    }
}
