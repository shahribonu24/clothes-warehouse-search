package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.entity.attribute.Gender;

/**
 * Template method: {@link #map} creates an empty product and lets every level of the
 * mapper hierarchy fill in the fields of its level of the entity hierarchy.
 */
public abstract class ProductMapper<P extends Product<P>> implements RecordMapper<P> {

    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String BRAND = "brand";
    protected static final String COLOR = "color";
    protected static final String GENDER = "gender";
    protected static final String MATERIAL = "material";
    protected static final String PRICE = "price";
    protected static final String QUANTITY = "quantity";

    @Override
    public final P map(CsvRecord record) {
        P product = create();
        fill(product, record);
        return product;
    }

    protected abstract P create();

    protected void fill(P product, CsvRecord record) {
        product.setId(record.getLong(ID))
                .setName(record.getString(NAME))
                .setBrand(record.getString(BRAND))
                .setColor(record.getString(COLOR))
                .setGender(record.getEnum(GENDER, Gender.class))
                .setMaterial(record.getString(MATERIAL))
                .setPrice(record.getDecimal(PRICE))
                .setQuantity(record.getInt(QUANTITY));
    }
}
