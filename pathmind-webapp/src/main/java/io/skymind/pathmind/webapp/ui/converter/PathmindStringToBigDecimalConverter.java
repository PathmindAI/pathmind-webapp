package io.skymind.pathmind.webapp.ui.converter;

import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PathmindStringToBigDecimalConverter extends StringToBigDecimalConverter
{
	public PathmindStringToBigDecimalConverter(String errorMessage) {
		super(errorMessage);
	}

	@Override
    protected NumberFormat getFormat(Locale locale) {
		DecimalFormat decimalFormat = new DecimalFormat("#.0");
		decimalFormat.setParseBigDecimal(true);
        return decimalFormat;
    }
}
