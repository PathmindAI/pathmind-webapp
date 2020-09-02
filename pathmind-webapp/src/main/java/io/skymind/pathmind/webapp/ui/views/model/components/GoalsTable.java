package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;

@CssImport(value = "./styles/components/goals-table.css")
public class GoalsTable extends CustomField<List<RewardVariable>> implements HasStyle {

    private List<RowField> goalFields = new ArrayList<>();
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
        container.add(LabelFactory.createLabel("Goal", "header-row"));

        container.add(LabelFactory.createLabel("Goal", "header-row"));

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
        private String goalOperatorSelectThemeNames = "goals small align-center";

        private RowField(boolean readOnly) {
            super(null);
            this.readOnly = readOnly;
            this.goalField = new NumberField();
            this.goalSpan = new Span();
            this.goalOperatorSelect = new Select<>();
            goalOperatorSelect.setItems("None", "\u2264", "\u2265");
            goalOperatorSelect.setValue("None");
            goalOperatorSelect.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            goalOperatorSelect.addValueChangeListener(event -> {
                setNumberFieldVisibility();
            });
            getContent().add(goalOperatorSelect);
            if (readOnly) {
                getContent().add(goalSpan);
            } else {
                goalField.addClassName("goal-field");
                goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                goalField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                goalField.addValueChangeListener(e -> {
                });
                getContent().add(goalField);
                setNumberFieldVisibility();
            }
            getContent().setWidthFull();
            GuiUtils.removeMarginsPaddingAndSpacing(getContent());
        }
        
        private void setNumberFieldVisibility() {
            Boolean goalFieldUsable = !goalOperatorSelect.getValue().equals("None");
            if (goalFieldUsable) {
                goalOperatorSelect.getElement().setAttribute("theme", goalOperatorSelectThemeNames+" not-none");
            } else {
                goalOperatorSelect.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            }
            goalField.setVisible(goalFieldUsable);
            goalField.setEnabled(goalFieldUsable);
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
            Double goalValue = newPresentationValue.getGoalValue();
            this.currentRewardVariable = newPresentationValue;
            if (goalValue != null) {
                if (readOnly) {
                    goalSpan.setText(""+goalValue);
                } else {
                    goalField.setValue(goalValue);
                }
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