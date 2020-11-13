package io.skymind.pathmind.webapp.ui.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

public class PathmindStringToBigDecimalConverter extends StringToBigDecimalConverter {
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
