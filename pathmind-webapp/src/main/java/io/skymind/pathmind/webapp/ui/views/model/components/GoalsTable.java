package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;

public class GoalsTable extends CustomField<List<RewardVariable>> implements HasStyle {

	private List<RowField> goalFields = new ArrayList<>();
    private List<RewardVariable> goalsList = new ArrayList<>();
    private VerticalLayout container;
    private Boolean isReadOnly;

    public GoalsTable(Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
	    container = new VerticalLayout();
	    container.setPadding(false);
	    container.setSpacing(false);
        container.setClassName("goals-table");
        add(container);
    }
    
    public void setItems(List<RewardVariable> goals) {
        goalsList = goals;

        for (int i = 0; i < goals.size(); i++) {
            container.add(createRow());
        }
    }

	private RowField createRow() {
		RowField goalField = new RowField(isReadOnly);
        goalFields.add(goalField);
		return goalField;
    }

    @Override
    protected List<RewardVariable> generateModelValue() {
        return goalFields.stream().map(RowField::getValue).collect(Collectors.toList());
    }

    @Override
    protected void setPresentationValue(List<RewardVariable> newPresentationValue) {
        newPresentationValue.forEach(rv -> goalFields.get(rv.getArrayIndex()).setValue(rv));
    }

	private static class RowField extends AbstractCompositeField<HorizontalLayout, RowField, RewardVariable> implements
			HasValidation {
        private final NumberField goalField;
        private final Span goalSpan;
        private final Select<String> goalOperatorSelect;
        private RewardVariable currentRewardVariable;
        private boolean readOnly = false;

		private RowField(boolean readOnly) {
            super(null);
            this.readOnly = readOnly;
            this.goalField = new NumberField();
            this.goalSpan = new Span();
            this.goalOperatorSelect = new Select<>();
            goalOperatorSelect.setItems("None", "\u2264", "\u2265");
            goalOperatorSelect.setValue("None");
            getContent().add(goalOperatorSelect);
            if (readOnly) {
                getContent().add(goalSpan);
            } else {
                goalField.addClassName("reward-variable-name-field");
                goalField.addValueChangeListener(e -> {
                });
                getContent().add(goalField);
            }
			getContent().setWidthFull();
			GuiUtils.removeMarginsPaddingAndSpacing(getContent());
		}

		@Override
		public RewardVariable getValue() {
            Double goalValue = goalField.getValue();
            Boolean goalIsLargerThanOrEqualTo = goalOperatorSelect.getValue() == "\u2265";
            currentRewardVariable.setGoalValue(goalValue);
            currentRewardVariable.setGoalIsLargerThanOrEqualTo(goalIsLargerThanOrEqualTo);
			return currentRewardVariable;
		}

		@Override
		protected void setPresentationValue(RewardVariable newPresentationValue) {
            Double goalValue = 0.0;
            this.currentRewardVariable = newPresentationValue;
            if (readOnly) {
                goalSpan.setText(""+goalValue);
            } else {
                goalField.setValue(goalValue);
            }
		}

		@Override
		public void setErrorMessage(String errorMessage) {
			goalField.setErrorMessage(errorMessage);
		}

		@Override
		public String getErrorMessage() {
			return goalField.getErrorMessage();
		}

		@Override
		public void setInvalid(boolean invalid) {
			goalField.setInvalid(invalid);
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
			return goalField.isInvalid();
		}
	}
}