package com.epam.warehouse.entity;

import com.epam.warehouse.entity.attribute.Gender;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base class of every product line stored in the warehouse.
 * <p>
 * The self-referencing type parameter lets fluent setters of the base class return
 * the concrete subclass type: {@code new Jacket().setName("Parka").setHooded(true)}.
 *
 * @param <SELF> concrete product type
 */
public abstract class Product<SELF extends Product<SELF>> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int PRICE_SCALE = 2;

    private long id;
    private String name;
    private String brand;
    private String color;
    private Gender gender;
    private String material;
    private BigDecimal price = BigDecimal.ZERO.setScale(PRICE_SCALE);
    private int quantity;

    protected Product() {
    }

    public abstract Category getCategory();

    public long getId() {
        return id;
    }

    public SELF setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be positive, but was " + id);
        }
        this.id = id;
        return self();
    }

    public String getName() {
        return name;
    }

    public SELF setName(String name) {
        this.name = requireText(name, "Name");
        return self();
    }

    public String getBrand() {
        return brand;
    }

    public SELF setBrand(String brand) {
        this.brand = requireText(brand, "Brand");
        return self();
    }

    public String getColor() {
        return color;
    }

    public SELF setColor(String color) {
        this.color = requireText(color, "Color");
        return self();
    }

    public Gender getGender() {
        return gender;
    }

    public SELF setGender(Gender gender) {
        this.gender = Objects.requireNonNull(gender, "Gender must not be null");
        return self();
    }

    public String getMaterial() {
        return material;
    }

    public SELF setMaterial(String material) {
        this.material = requireText(material, "Material");
        return self();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public SELF setPrice(BigDecimal price) {
        Objects.requireNonNull(price, "Price must not be null");
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price can't be negative, but was " + price);
        }
        this.price = price.setScale(PRICE_SCALE, RoundingMode.HALF_UP);
        return self();
    }

    public int getQuantity() {
        return quantity;
    }

    public SELF setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can't be negative, but was " + quantity);
        }
        this.quantity = quantity;
        return self();
    }

    /**
     * @return value of all units of this product line in stock (price × quantity)
     */
    public BigDecimal getStockValue() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    /**
     * Attributes that are specific to a branch of the hierarchy.
     * Every subclass adds its own attributes after the attributes of its parent.
     *
     * @return ordered map "attribute name → value"
     */
    public Map<String, Object> specificAttributes() {
        return new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    protected final SELF self() {
        return (SELF) this;
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product<?> other = (Product<?>) o;
        return id == other.id
                && quantity == other.quantity
                && Objects.equals(name, other.name)
                && Objects.equals(brand, other.brand)
                && Objects.equals(color, other.color)
                && gender == other.gender
                && Objects.equals(material, other.material)
                && Objects.equals(price, other.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, color, gender, material, price, quantity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName())
                .append("{id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", brand='").append(brand).append('\'')
                .append(", color='").append(color).append('\'')
                .append(", gender=").append(gender)
                .append(", material='").append(material).append('\'')
                .append(", price=").append(price)
                .append(", quantity=").append(quantity)
                .append(", stockValue=").append(getStockValue());
        specificAttributes().forEach((key, value) -> sb.append(", ").append(key).append('=').append(value));
        return sb.append('}').toString();
    }
}
