package com.epam.warehouse.entity;

import java.util.Map;
import java.util.Objects;

public class Boots extends Footwear<Boots> implements Waterproof {

    private static final long serialVersionUID = 1L;

    private boolean waterproof;
    private int shaftHeight;

    public Boots() {
    }

    @Override
    public Category getCategory() {
        return Category.BOOTS;
    }

    @Override
    public boolean isWaterproof() {
        return waterproof;
    }

    public Boots setWaterproof(boolean waterproof) {
        this.waterproof = waterproof;
        return this;
    }

    /**
     * @return height of the boot shaft in centimeters
     */
    public int getShaftHeight() {
        return shaftHeight;
    }

    public Boots setShaftHeight(int shaftHeight) {
        if (shaftHeight <= 0) {
            throw new IllegalArgumentException("Shaft height must be positive, but was " + shaftHeight);
        }
        this.shaftHeight = shaftHeight;
        return this;
    }

    @Override
    public Map<String, Object> specificAttributes() {
        Map<String, Object> attributes = super.specificAttributes();
        attributes.put("waterproof", waterproof);
        attributes.put("shaftHeight", shaftHeight + "cm");
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Boots other = (Boots) o;
        return waterproof == other.waterproof && shaftHeight == other.shaftHeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), waterproof, shaftHeight);
    }
}
