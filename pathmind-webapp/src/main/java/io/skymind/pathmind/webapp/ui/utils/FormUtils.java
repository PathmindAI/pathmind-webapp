package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class FormUtils
{
	private FormUtils() {
	}

	public static boolean isValidForm(Binder binder, Object pojo) {
		try {
			binder.writeBean(pojo);
			return true;
		} catch (ValidationException e) {
			return false;
		}
	}
}
