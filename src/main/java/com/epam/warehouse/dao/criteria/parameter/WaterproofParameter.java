package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Waterproof;

import java.util.Set;

/**
 * Applicable to every product implementing {@link Waterproof}, regardless of its branch
 * in the class hierarchy (jackets and boots).
 */
public class WaterproofParameter extends ValueSetParameter<Waterproof, Boolean> {

    public WaterproofParameter(boolean waterproof) {
        super("waterproof", Waterproof.class, Waterproof::isWaterproof, Set.of(waterproof));
    }
}
