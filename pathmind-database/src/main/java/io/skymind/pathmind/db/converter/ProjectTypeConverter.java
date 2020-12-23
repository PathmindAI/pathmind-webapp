package io.skymind.pathmind.db.converter;

import io.skymind.pathmind.shared.data.ProjectType;
import org.jooq.impl.EnumConverter;

public class ProjectTypeConverter extends EnumConverter<Integer, ProjectType> {

    public ProjectTypeConverter() {
        super(Integer.class, ProjectType.class);
    }
}
