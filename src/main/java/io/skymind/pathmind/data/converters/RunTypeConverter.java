package io.skymind.pathmind.data.converters;

import io.skymind.pathmind.constants.RunType;
import org.jooq.impl.EnumConverter;

public class RunTypeConverter extends EnumConverter<Integer, RunType>
{
	public RunTypeConverter() {
		super(Integer.class, RunType.class);
	}
}
