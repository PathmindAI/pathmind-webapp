package io.skymind.pathmind.db.converter;

import io.skymind.pathmind.shared.constants.ParamType;
import org.jooq.impl.EnumConverter;

public class SimulationParamTypeConverter extends EnumConverter<Integer, ParamType> {

    public SimulationParamTypeConverter(Class<Integer> fromType, Class<ParamType> toType) {
        super(fromType, toType);
    }

    @Override
    public Integer to(ParamType paramType) {
        if (paramType == null) {
            return null;
        } else {
            return paramType.getValue();
        }
    }

}
