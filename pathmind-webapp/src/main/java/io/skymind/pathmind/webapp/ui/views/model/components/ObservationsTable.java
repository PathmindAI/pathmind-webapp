package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/observations-table.css")
public class ObservationsTable extends CustomField<List<Observation>> implements HasStyle {

	private List<RowField> observationFields = new ArrayList<>();

	private VerticalLayout container;

	public ObservationsTable() {
	    setClassName("observations-table");
		container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		container.setClassName("observations-wrapper");

		add(container);
	}

	private RowField createRow(int rowNumber) {
		RowField rowField = new RowField(rowNumber);
		observationFields.add(rowField);
		return rowField;
	}

	@Override
	protected List<Observation> generateModelValue() {
	    return observationFields.stream().map(RowField::getValue).filter(val -> val != null).collect(Collectors.toList());
	}

	@Override
	public void setPresentationValue(List<Observation> newPresentationValue) {
		newPresentationValue.forEach(rv -> observationFields.get(rv.getArrayIndex()).setValue(rv));
	}

	@Override
	public boolean isInvalid() {
		return observationFields.stream().anyMatch(f -> !f.isValid());
	}

    public void setNumberOfItems(int numberOfPossibleActions) {
        container.removeAll();
        observationFields.clear();
        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("#"), new Span("Observation"));

        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

        container.add(headerRow);

        for (int i = 0; i < numberOfPossibleActions; i++) {
            container.add(createRow(i));
        }
    }

	private static class RowField extends AbstractCompositeField<HorizontalLayout, RowField, Observation> {

		private final int rowNumber;

		private TextField variable;
		private TextField description;
		private ComboBox<ObservationDataType> dataType;
		private TextField example;
		private NumberField min;
		private NumberField max;
		private NumberField minItems;
		private NumberField maxItems;
		
		private Binder<Observation> binder;
		private Observation value;

		private RowField(int rowNumber) {
			super(null);
			this.rowNumber = rowNumber;
			createLayout();
			initBinder();
			dataType.addValueChangeListener(evt -> arrangeFieldVisibility(evt.getValue()));
			arrangeFieldVisibility(null);
		}

        private void createLayout() {
		    variable = new TextField();
		    variable.setPlaceholder("Variable name");
            description = new TextField();
            description.setPlaceholder("Description");
            dataType = new ComboBox<>();
            dataType.setItems(ObservationDataType.values());
            dataType.setPlaceholder("Data type");
            dataType.getElement().setAttribute("theme", "code");
            example = new TextField();
            example.setPlaceholder("Ex.");
            min = new NumberField();
            min.setPlaceholder("Min.");
            max = new NumberField();
            max.setPlaceholder("Max.");
            minItems = new NumberField();
            minItems.setPlaceholder("Min Items");
            maxItems = new NumberField();
            maxItems.setPlaceholder("Max Items");
            
            FormLayout form = new FormLayout();
            form.setResponsiveSteps(new ResponsiveStep("1px", 5));
            form.addClassName("action-name-"+rowNumber);
            form.add(variable, 2);
            form.add(description, 3);
            form.add(dataType, 2);
            form.add(example, min, max, minItems, maxItems);
            
            getContent().add(new Span("" + rowNumber), form);
            getContent().setWidthFull();
            GuiUtils.removeMarginsPaddingAndSpacing(getContent());
        }
        
        private void initBinder() {
            binder = new Binder<>();
            binder.forField(variable)
                .asRequired("Variable name is mandatory")
                .withValidator(new StringLengthValidator("Variable name must not exceed 16 characters.", 0, 16))
                .bind(Observation::getVariable, Observation::setVariable);
            binder.forField(description)
                .withValidator(new StringLengthValidator("Variable name must not exceed 100 characters.", 0, 100))
                .bind(Observation::getDescription, Observation::setDescription);
            binder.forField(dataType)
                .asRequired("Data type is mandatory")
                .bind(Observation::getDataTypeEnum, Observation::setDataTypeEnum);
            binder.forField(example)
                .withValidator(new StringLengthValidator("Example must not exceed 100 characters.", 0, 100))
                .bind(Observation::getExample, Observation::setExample);
            binder.forField(min)
                .bind(Observation::getMin, Observation::setMin);
            binder.forField(max)
                .bind(Observation::getMax, Observation::setMax);
            binder.forField(minItems)
                .bind(Observation::getMinItems, Observation::setMinItems);
            binder.forField(maxItems)
                .bind(Observation::getMaxItems, Observation::setMaxItems);
        }

        public boolean isValid() {
            return !binder.hasChanges() || binder.validate().isOk();
        }

		@Override
		public Observation getValue() {
		    if (binder.hasChanges()) {
		        if (value == null) {
		            value = new Observation();
		        }
		        value.setArrayIndex(rowNumber);
		        binder.writeBeanIfValid(value);
		    }
			return value;
		}

		@Override
		protected void setPresentationValue(Observation newPresentationValue) {
		    value = newPresentationValue;
			binder.readBean(newPresentationValue);
		}
		
		private void arrangeFieldVisibility(ObservationDataType type) {
		    min.setVisible(type != null && ObservationDataType.isNumeric(type));
		    max.setVisible(type != null && ObservationDataType.isNumeric(type));
		    minItems.setVisible(type != null && ObservationDataType.isArray(type));
		    maxItems.setVisible(type != null && ObservationDataType.isArray(type));
		}
	}
}
