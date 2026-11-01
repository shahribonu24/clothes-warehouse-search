package com.epam.warehouse.entity;

import java.util.Map;
import java.util.Objects;

/**
 * Footwear uses EU numeric sizes (half sizes are allowed).
 */
public abstract class Footwear<SELF extends Footwear<SELF>> extends Product<SELF> {

    private static final long serialVersionUID = 1L;
    public static final double MIN_SHOE_SIZE = 16.0;
    public static final double MAX_SHOE_SIZE = 50.0;

    private double shoeSize;

    protected Footwear() {
    }

    public double getShoeSize() {
        return shoeSize;
    }

    public SELF setShoeSize(double shoeSize) {
        if (shoeSize < MIN_SHOE_SIZE || shoeSize > MAX_SHOE_SIZE || shoeSize * 2 != Math.rint(shoeSize * 2)) {
            throw new IllegalArgumentException("Shoe size must be a whole or half number between "
                    + MIN_SHOE_SIZE + " and " + MAX_SHOE_SIZE + ", but was " + shoeSize);
        }
        this.shoeSize = shoeSize;
        return self();
    }

    @Override
    public Map<String, Object> specificAttributes() {
        Map<String, Object> attributes = super.specificAttributes();
        attributes.put("shoeSize", shoeSize);
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        return Double.compare(shoeSize, ((Footwear<?>) o).shoeSize) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shoeSize);
    }
}
