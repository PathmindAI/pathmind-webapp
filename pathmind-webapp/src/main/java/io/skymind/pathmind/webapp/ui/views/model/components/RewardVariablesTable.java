package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardVariablesTable extends CustomField<List<RewardVariable>> {

	private List<TextField> rewardVariableNameFields = new ArrayList<>();
	private VerticalLayout container;
	private List<RewardVariable> rewardVariables;
	
	public RewardVariablesTable() {
		rewardVariables = new ArrayList<RewardVariable>();
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
		for (int i = 0; i < rewardVariableNameFields.size(); i++) {
			if (rewardVariables.size() <= i) {
				rewardVariables.add(new RewardVariable());
			}
			rewardVariables.get(i).setName(rewardVariableNameFields.get(i).getValue());
			rewardVariables.get(i).setArrayIndex(i);
		}
		return rewardVariables;
	}

	@Override
	protected void setPresentationValue(
			List<RewardVariable> newPresentationValue) {
		rewardVariables = newPresentationValue;
		setVariableSize(rewardVariables.size());
		for (int i = 0; i < rewardVariables.size(); i++) {
			rewardVariableNameFields.get(i).setValue(rewardVariables.get(i).getName());
		}
	}
}
