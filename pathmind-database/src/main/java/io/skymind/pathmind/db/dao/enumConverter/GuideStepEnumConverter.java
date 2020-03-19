package io.skymind.pathmind.db.dao.enumConverter;

import io.skymind.pathmind.shared.constants.GuideStep;
import org.jooq.impl.EnumConverter;

public class GuideStepEnumConverter extends EnumConverter<Integer, GuideStep> {
    public GuideStepEnumConverter() {
        super(Integer.class, GuideStep.class);
    }
}
