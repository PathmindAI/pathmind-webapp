package io.skymind.pathmind.ui.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Automatically trims the values retrieved from input element 
 */
public class TrimmedStringConverter implements Converter<String, String>{

	@Override
	public Result<String> convertToModel(String value, ValueContext context) {
		if (value.isEmpty()) {
			return Result.ok("");
		} else {
			return Result.ok(value.trim());
		}
	}

	@Override
	public String convertToPresentation(String value, ValueContext context) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

}
