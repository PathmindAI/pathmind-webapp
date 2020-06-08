package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.List;

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
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends CustomField<List<RewardVariable>> implements HasStyle {

	private List<RowField> rewardVariableNameFields = new ArrayList<>();

	private VerticalLayout container;

	public RewardVariablesTable() {
		container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		container.setClassName("reward-variables-table");

		add(container);
	}

	public void setCodeEditorMode() {
		setClassName("with-container-border");
	}

	public void setVariableSize(int numOfVariables) {
		container.removeAll();
		rewardVariableNameFields.clear();
		HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("#"), new Span("Variable Name"));

		headerRow.addClassName("header-row");
		GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

		container.add(headerRow);

		for (int i = 0; i < numOfVariables; i++) {
			container.add(createRow(i));
		}
	}

	private RowField createRow(int rowNumber) {
		RowField rewardVariableNameField = new RowField(rowNumber);
		rewardVariableNameFields.add(rewardVariableNameField);
		FormUtils.addValidator(rewardVariableNameField, new RowValidator());
		return rewardVariableNameField;
	}

	@Override
	protected List<RewardVariable> generateModelValue() {
		List<RewardVariable> modelValue = new ArrayList<>();
		for (int i = 0; i < rewardVariableNameFields.size(); i++) {
			modelValue.add(rewardVariableNameFields.get(i).getValue());
		}
		return modelValue;
	}

	@Override
	public void setPresentationValue(List<RewardVariable> newPresentationValue) {
		setVariableSize(newPresentationValue.size());
		newPresentationValue.forEach(rv -> rewardVariableNameFields.get(rv.getArrayIndex()).setValue(rv));
	}

	@Override
	public boolean isInvalid() {
		return rewardVariableNameFields.stream().anyMatch(f -> f.isInvalid());
	}

	private static class RowValidator implements Validator<RewardVariable> {
		private final StringLengthValidator nameValidator = new StringLengthValidator("Variable name must not exceed 100 characters", 0, 100);

		@Override
		public ValidationResult apply(RewardVariable rewardVariable, ValueContext valueContext) {
			return nameValidator.apply(rewardVariable.getName(), valueContext);
		}
	}

	private static class RowField extends AbstractCompositeField<HorizontalLayout, RowField, RewardVariable> implements
			HasValidation {

		private long modelId = 0;

		private final int rowNumber;

		private final TextField rewardVariableNameField;

		private RowField(int rowNumber) {
			super(null);
			this.rowNumber = rowNumber;
			this.rewardVariableNameField = new TextField();
			rewardVariableNameField.addClassName("reward-variable-name-field");
			rewardVariableNameField.addClassName("reward-variable-"+rowNumber);
			rewardVariableNameField.addValueChangeListener(e -> {
				ComponentValueChangeEvent<RowField, RewardVariable> newEvent = new ComponentValueChangeEvent<>(
						this, this, create(e.getOldValue()), e.isFromClient());
				fireEvent(newEvent);
			});
			getContent().add(new Span("" + rowNumber), rewardVariableNameField);
			getContent().setWidthFull();
			GuiUtils.removeMarginsPaddingAndSpacing(getContent());
		}

		private RewardVariable create(String value) {
			return new RewardVariable(modelId, value, rowNumber);
		}

		@Override
		public RewardVariable getValue() {
			return create(rewardVariableNameField.getValue().trim());
		}

		@Override
		protected void setPresentationValue(RewardVariable newPresentationValue) {
			modelId = newPresentationValue.getModelId();
			rewardVariableNameField.setValue(newPresentationValue.getName());
		}

		@Override
		public void setErrorMessage(String errorMessage) {
			rewardVariableNameField.setErrorMessage(errorMessage);
		}

		@Override
		public String getErrorMessage() {
			return rewardVariableNameField.getErrorMessage();
		}

		@Override
		public void setInvalid(boolean invalid) {
			rewardVariableNameField.setInvalid(invalid);
			String className = "invalid-reward-variable-row";
			if (invalid) {
				getContent().addClassName(className);
			}
			else {
				getContent().removeClassName(className);
			}
		}

		@Override
		public boolean isInvalid() {
			return rewardVariableNameField.isInvalid();
		}
	}
}
