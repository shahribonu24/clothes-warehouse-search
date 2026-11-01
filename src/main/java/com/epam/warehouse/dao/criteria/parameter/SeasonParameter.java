package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Clothing;
import com.epam.warehouse.entity.attribute.Season;

import java.util.Collection;

public class SeasonParameter extends ValueSetParameter<Clothing<?>, Season> {

    public SeasonParameter(Collection<Season> seasons) {
        super("season", Clothing.class, Clothing::getSeason, seasons);
    }
}
