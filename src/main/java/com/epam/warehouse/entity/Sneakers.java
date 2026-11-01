package com.epam.warehouse.entity;

import com.epam.warehouse.entity.attribute.SportType;

import java.util.Map;
import java.util.Objects;

public class Sneakers extends Footwear<Sneakers> {

    private static final long serialVersionUID = 1L;

    private SportType sportType;

    public Sneakers() {
    }

    @Override
    public Category getCategory() {
        return Category.SNEAKERS;
    }

    public SportType getSportType() {
        return sportType;
    }

    public Sneakers setSportType(SportType sportType) {
        this.sportType = Objects.requireNonNull(sportType, "Sport type must not be null");
        return this;
    }

    @Override
    public Map<String, Object> specificAttributes() {
        Map<String, Object> attributes = super.specificAttributes();
        attributes.put("sport", sportType);
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        return sportType == ((Sneakers) o).sportType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sportType);
    }
}
