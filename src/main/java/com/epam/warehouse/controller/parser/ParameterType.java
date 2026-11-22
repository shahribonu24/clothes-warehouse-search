package com.epam.warehouse.controller.parser;

import com.epam.warehouse.dao.criteria.parameter.BrandParameter;
import com.epam.warehouse.dao.criteria.parameter.ColorParameter;
import com.epam.warehouse.dao.criteria.parameter.FitParameter;
import com.epam.warehouse.dao.criteria.parameter.GenderParameter;
import com.epam.warehouse.dao.criteria.parameter.HoodedParameter;
import com.epam.warehouse.dao.criteria.parameter.IdParameter;
import com.epam.warehouse.dao.criteria.parameter.LengthParameter;
import com.epam.warehouse.dao.criteria.parameter.MaterialParameter;
import com.epam.warehouse.dao.criteria.parameter.NameParameter;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.dao.criteria.parameter.QuantityParameter;
import com.epam.warehouse.dao.criteria.parameter.SeasonParameter;
import com.epam.warehouse.dao.criteria.parameter.ShaftHeightParameter;
import com.epam.warehouse.dao.criteria.parameter.ShoeSizeParameter;
import com.epam.warehouse.dao.criteria.parameter.SizeParameter;
import com.epam.warehouse.dao.criteria.parameter.SportTypeParameter;
import com.epam.warehouse.dao.criteria.parameter.WaistParameter;
import com.epam.warehouse.dao.criteria.parameter.WaterproofParameter;
import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Clothing;
import com.epam.warehouse.entity.Footwear;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.entity.Sneakers;
import com.epam.warehouse.entity.Waterproof;
import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Fit;
import com.epam.warehouse.entity.attribute.Gender;
import com.epam.warehouse.entity.attribute.Season;
import com.epam.warehouse.entity.attribute.SportType;
import com.epam.warehouse.util.EnumParser;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Every search parameter the user can type: its name, value format, the most general product type
 * it applies to, and a factory that turns the text value into a DAO {@link Parameter}.
 * Adding a new parameter = adding one constant here.
 */
public enum ParameterType {
    ID("id", "number or range, e.g. 5 or 1;10", Product.class,
            v -> new IdParameter(ValueParser.longRange(v))),
    NAME("name", "text, part of the name (use quotes for spaces)", Product.class, NameParameter::new),
    BRAND("brand", "text, exact brand", Product.class, BrandParameter::new),
    COLOR("color", "text, exact color", Product.class, ColorParameter::new),
    MATERIAL("material", "text, part of the material", Product.class, MaterialParameter::new),
    GENDER("gender", "one or more of " + EnumParser.allowedValues(Gender.class), Product.class,
            v -> new GenderParameter(ValueParser.enumSet(Gender.class, v))),
    PRICE("price", "number or range, e.g. 50;150, ;100, 20;", Product.class,
            v -> new PriceParameter(ValueParser.decimalRange(v))),
    QUANTITY("quantity", "number or range, e.g. 0 or 5;", Product.class,
            v -> new QuantityParameter(ValueParser.intRange(v))),
    SIZE("size", "one or more of " + EnumParser.allowedValues(ClothingSize.class), Clothing.class,
            v -> new SizeParameter(ValueParser.enumSet(ClothingSize.class, v))),
    SEASON("season", "one or more of " + EnumParser.allowedValues(Season.class), Clothing.class,
            v -> new SeasonParameter(ValueParser.enumSet(Season.class, v))),
    WATERPROOF("waterproof", "true or false", Waterproof.class,
            v -> new WaterproofParameter(ValueParser.bool(v))),
    HOODED("hooded", "true or false", Jacket.class, v -> new HoodedParameter(ValueParser.bool(v))),
    WAIST("waist", "inches, number or range, e.g. 30;34", Jeans.class,
            v -> new WaistParameter(ValueParser.intRange(v))),
    LENGTH("length", "inches, number or range, e.g. 32", Jeans.class,
            v -> new LengthParameter(ValueParser.intRange(v))),
    FIT("fit", "one or more of " + EnumParser.allowedValues(Fit.class), Jeans.class,
            v -> new FitParameter(ValueParser.enumSet(Fit.class, v))),
    SHOE_SIZE("shoesize", "EU size, number or range, e.g. 42 or 40;43.5", Footwear.class,
            v -> new ShoeSizeParameter(ValueParser.doubleRange(v))),
    SPORT("sport", "one or more of " + EnumParser.allowedValues(SportType.class), Sneakers.class,
            v -> new SportTypeParameter(ValueParser.enumSet(SportType.class, v))),
    HEIGHT("height", "shaft height in cm, number or range, e.g. 20;", Boots.class,
            v -> new ShaftHeightParameter(ValueParser.intRange(v)));

    private final String key;
    private final String format;
    private final Class<?> targetType;
    private final Function<String, Parameter<?>> factory;

    ParameterType(String key, String format, Class<?> targetType, Function<String, Parameter<?>> factory) {
        this.key = key;
        this.format = format;
        this.targetType = targetType;
        this.factory = factory;
    }

    public String key() {
        return key;
    }

    public String format() {
        return format;
    }

    /**
     * @param value text value typed by the user
     * @throws IllegalArgumentException if the value has a wrong format
     */
    public Parameter<?> create(String value) {
        return factory.apply(value);
    }

    /**
     * @return keys of the categories this parameter can be used with, e.g. "jackets, boots"
     */
    public String applicableCategories() {
        return Arrays.stream(Category.values())
                .filter(c -> targetType.isAssignableFrom(c.type()))
                .map(Category::key)
                .collect(Collectors.joining(", "));
    }

    public static Optional<ParameterType> byKey(String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = key.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(p -> p.key.equals(normalized)).findFirst();
    }
}
