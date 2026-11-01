package com.epam.warehouse.entity;

import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Season;

import java.util.Map;
import java.util.Objects;

/**
 * Clothing uses letter sizes and is designed for a particular season.
 */
public abstract class Clothing<SELF extends Clothing<SELF>> extends Product<SELF> {

    private static final long serialVersionUID = 1L;

    private ClothingSize size;
    private Season season;

    protected Clothing() {
    }

    public ClothingSize getSize() {
        return size;
    }

    public SELF setSize(ClothingSize size) {
        this.size = Objects.requireNonNull(size, "Size must not be null");
        return self();
    }

    public Season getSeason() {
        return season;
    }

    public SELF setSeason(Season season) {
        this.season = Objects.requireNonNull(season, "Season must not be null");
        return self();
    }

    @Override
    public Map<String, Object> specificAttributes() {
        Map<String, Object> attributes = super.specificAttributes();
        attributes.put("size", size);
        attributes.put("season", season);
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Clothing<?> other = (Clothing<?>) o;
        return size == other.size && season == other.season;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), size, season);
    }
}
