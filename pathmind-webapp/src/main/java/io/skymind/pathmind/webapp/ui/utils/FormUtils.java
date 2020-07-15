package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

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

	public static <T extends Component & HasValue & HasValidation> void addValidator(T field, Validator validator) {
		field.addValueChangeListener(event -> {
			ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));

			if (result.isError()) {
				field.setInvalid(true);
				field.setErrorMessage(result.getErrorMessage());
			} else {
				field.setInvalid(false);
				field.setErrorMessage(null);
			}
		});
	}
}
