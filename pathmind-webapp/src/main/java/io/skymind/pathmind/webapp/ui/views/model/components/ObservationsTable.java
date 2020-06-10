package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.StringLengthValidator;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import java.util.ArrayList;
import java.util.List;

@CssImport(value = "./styles/components/actions-table.css")
public class ObservationsTable extends CustomField<List<Observation>> implements HasStyle {

	private List<RowField> observationFields = new ArrayList<>();

	private VerticalLayout container;

	public ObservationsTable() {
		container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		container.setClassName("actions-table");

		add(container);
	}

	private RowField createRow(int rowNumber) {
		RowField rowField = new RowField(rowNumber);
		observationFields.add(rowField);
		FormUtils.addValidator(rowField, new RowValidator());
		return rowField;
	}

	@Override
	protected List<Observation> generateModelValue() {
		List<Observation> modelValue = new ArrayList<>();
        for (RowField observationField : observationFields) {
            modelValue.add(observationField.getValue());
        }
		return modelValue;
	}

	@Override
	public void setPresentationValue(List<Observation> newPresentationValue) {
        setNumberOfItems(newPresentationValue.size());
//		newPresentationValue.forEach(rv -> observationFields.get(rv.getArrayIndex()).setValue(rv));
	}

	@Override
	public boolean isInvalid() {
		return observationFields.stream().anyMatch(f -> f.isInvalid());
	}

    public void setNumberOfItems(int numberOfPossibleActions) {
        container.removeAll();
        observationFields.clear();
        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("#"), new Span("Action Name"));

        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

        container.add(headerRow);

        for (int i = 0; i < numberOfPossibleActions; i++) {
            container.add(createRow(i));
        }
    }

    private static class RowValidator implements Validator<Observation> {
		private final StringLengthValidator nameValidator = new StringLengthValidator("Observation's Variable name must not exceed 100 characters", 0, 100);

		@Override
		public ValidationResult apply(Observation observation, ValueContext valueContext) {
			return nameValidator.apply(observation.getVariable(), valueContext);
		}
	}

	private static class RowField extends AbstractCompositeField<HorizontalLayout, RowField, Observation> implements
			HasValidation {

		private long modelId = 0;

		private final int rowNumber;

		private final TextField theField;

		private RowField(int rowNumber) {
			super(null);
			this.rowNumber = rowNumber;
			this.theField = new TextField();
			theField.addClassName("action-name-field");
			theField.addClassName("action-name-"+rowNumber);
			theField.addValueChangeListener(e -> {
				ComponentValueChangeEvent<RowField, Observation> newEvent = new ComponentValueChangeEvent<>(
						this, this, create(e.getOldValue()), e.isFromClient());
				fireEvent(newEvent);
			});
			getContent().add(new Span("" + rowNumber), theField);
			getContent().setWidthFull();
			GuiUtils.removeMarginsPaddingAndSpacing(getContent());
		}

		private Observation create(String value) {
            Observation observation = new Observation();
            observation.setModelId(modelId);
            observation.setVariable(value);
			return observation;
		}

		@Override
		public Observation getValue() {
			return create(theField.getValue().trim());
		}

		@Override
		protected void setPresentationValue(Observation newPresentationValue) {
			modelId = newPresentationValue.getModelId();
			theField.setValue(newPresentationValue.getVariable());
		}

		@Override
		public void setErrorMessage(String errorMessage) {
			theField.setErrorMessage(errorMessage);
		}

		@Override
		public String getErrorMessage() {
			return theField.getErrorMessage();
		}

		@Override
		public void setInvalid(boolean invalid) {
			theField.setInvalid(invalid);
			String className = "invalid-action-row";
			if (invalid) {
				getContent().addClassName(className);
			}
			else {
				getContent().removeClassName(className);
			}
		}

		@Override
		public boolean isInvalid() {
			return theField.isInvalid();
		}
	}
}
