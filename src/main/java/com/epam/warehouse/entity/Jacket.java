package com.epam.warehouse.entity;

import java.util.Map;
import java.util.Objects;

public class Jacket extends Clothing<Jacket> implements Waterproof {

    private static final long serialVersionUID = 1L;

    private boolean waterproof;
    private boolean hooded;

    public Jacket() {
    }

    @Override
    public Category getCategory() {
        return Category.JACKETS;
    }

    @Override
    public boolean isWaterproof() {
        return waterproof;
    }

    public Jacket setWaterproof(boolean waterproof) {
        this.waterproof = waterproof;
        return this;
    }

    public boolean isHooded() {
        return hooded;
    }

    public Jacket setHooded(boolean hooded) {
        this.hooded = hooded;
        return this;
    }

    @Override
    public Map<String, Object> specificAttributes() {
        Map<String, Object> attributes = super.specificAttributes();
        attributes.put("waterproof", waterproof);
        attributes.put("hooded", hooded);
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Jacket other = (Jacket) o;
        return waterproof == other.waterproof && hooded == other.hooded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), waterproof, hooded);
    }
}
