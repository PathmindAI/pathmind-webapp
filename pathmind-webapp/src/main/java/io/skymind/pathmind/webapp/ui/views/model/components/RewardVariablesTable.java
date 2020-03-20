package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends CustomField<List<RewardVariable>> {

	private List<TextField> rewardVariableNameFields = new ArrayList<>();
	private VerticalLayout container;

	private long modelId = 0;
	
	public RewardVariablesTable() {
		container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		container.setClassName("reward-variables-table");

		add(container);
	}
	
	public void setVariableSize(int numOfVariables) {
		container.removeAll();
		rewardVariableNameFields.clear();
		HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(
				new Span("#"),
				new Span("Variable Name")
			);
		
		headerRow.addClassName("header-row");
		GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

		container.add(headerRow);

		for (int i = 0; i < numOfVariables; i++) {
			container.add(createRow(i));
		}
	}
	
	private HorizontalLayout createRow(int rowNumber) {
		TextField rewardVariableNameField = new TextField();
		rewardVariableNameField.addClassName("reward-variable-name-field");
		HorizontalLayout row = WrapperUtils.wrapWidthFullHorizontal(
			new Span(""+rowNumber),
			rewardVariableNameField
		);
		rewardVariableNameFields.add(rewardVariableNameField);
		GuiUtils.removeMarginsPaddingAndSpacing(row);
		return row;
	}

	@Override
	protected List<RewardVariable> generateModelValue() {
		List<RewardVariable> modelValue = new ArrayList<>();
		for (int i = 0; i < rewardVariableNameFields.size(); i++) {
			modelValue.add(new RewardVariable(modelId, rewardVariableNameFields.get(i).getValue(), i));
		}
		return modelValue;
	}

	@Override
	public void setPresentationValue(
			List<RewardVariable> newPresentationValue) {
		modelId = !newPresentationValue.isEmpty() ? newPresentationValue.get(0).getModelId() : 0;
		setVariableSize(newPresentationValue.size());
		newPresentationValue.forEach(rv -> rewardVariableNameFields.get(rv.getArrayIndex()).setValue(rv.getName()));
	}
}
