package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.textfield.NumberField;

public class VaadinUtils
{
	private VaadinUtils() {
	}

	public static NumberField generateNumberField(long min, long max, long value) {
		NumberField numberField = new NumberField();
		numberField.setHasControls(true);
		numberField.setMin(min);
		numberField.setMax(max);
		numberField.setValue((double)value);
		return numberField;
	}
}
