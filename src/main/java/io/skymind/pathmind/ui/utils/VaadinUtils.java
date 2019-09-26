package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.UI;
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

	/**
	 * Until Vaadin has the ability to get the view names https://github.com/vaadin/flow/issues/1897 this is a workaround.
	 */
	public static String getViewName() {
		return UI.getCurrent().getInternals().getActiveViewLocation().getFirstSegment();
	}
}
