package com.epam.warehouse.entity;

/**
 * Behaviour shared by products from different branches of the hierarchy
 * (jackets and boots) that may be protected against water.
 */
public interface Waterproof {
    boolean isWaterproof();
}
