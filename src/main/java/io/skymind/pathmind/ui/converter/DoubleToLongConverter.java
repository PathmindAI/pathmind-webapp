package io.skymind.pathmind.ui.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class DoubleToLongConverter implements Converter<Double, Long>
{
	@Override
	public Result<Long> convertToModel(Double presentation, ValueContext valueContext) {
		return Result.ok(presentation.longValue());
	}

	@Override
	public Double convertToPresentation(Long model, ValueContext valueContext) {
		return model.doubleValue();
	}
}