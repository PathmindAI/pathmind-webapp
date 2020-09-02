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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;

@CssImport(value = "./styles/components/goals-table.css")
public class GoalsTable extends CustomField<List<RewardVariable>> implements HasStyle {

    private List<RowField> goalFields = new ArrayList<>();
    private VerticalLayout container;

    public GoalsTable() {
        container = new VerticalLayout();
        container.setPadding(false);
        container.setSpacing(false);
        container.setClassName("goals-table");
        add(container);
    }
    
    private RowField createRow() {
        RowField goalField = new RowField();
        goalFields.add(goalField);
        return goalField;
    }

    @Override
    protected List<RewardVariable> generateModelValue() {
        return goalFields.stream().map(RowField::getValue).collect(Collectors.toList());
    }

    @Override
    protected void setPresentationValue(List<RewardVariable> newPresentationValue) {
        goalFields.clear();
        container.removeAll();
        
        container.add(LabelFactory.createLabel("Goal", "header-row"));
        newPresentationValue.forEach(rv -> {
            RowField field = createRow();
            field.setValue(rv);
            container.add(field);
        });
    }
    
    public boolean isValid() {
        return goalFields.stream().allMatch(f -> !f.isInvalid());
    }

    private static class RowField extends AbstractCompositeField<HorizontalLayout, RowField, RewardVariable> implements
            HasValidation {
        private final NumberField goalField;
        private final Span goalSpan;
        private final Select<GoalConditionType> conditionType;
        private Binding<RewardVariable, Double> goalValueBinding;
        private String goalOperatorSelectThemeNames = "goals small align-center";
        
        private Binder<RewardVariable> binder;

        private RowField() {
            super(null);
            this.goalField = new NumberField();
            this.goalSpan = new Span();
            this.conditionType = new Select<>();

            createLayout();
            initBinder();
            conditionType.addValueChangeListener(event -> setNumberFieldVisibility());
            setNumberFieldVisibility();
        }
        
        private void createLayout() {
            conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
            conditionType.setItemLabelGenerator(type -> type != null ? type.toString() : "None");
            conditionType.setEmptySelectionAllowed(true);
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            getContent().add(conditionType);
            getContent().add(goalSpan);
            goalSpan.setVisible(false);
            goalField.addClassName("goal-field");
            goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
            goalField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
            getContent().add(goalField);
            getContent().setWidthFull();
            GuiUtils.removeMarginsPaddingAndSpacing(getContent());
        }
        
        private void initBinder() {
            binder = new Binder<>();
            binder.bind(conditionType, RewardVariable::getGoalConditionTypeEnum, RewardVariable::setGoalConditionTypeEnum);
            goalValueBinding = binder.forField(goalField).asRequired("Enter a value for goal").bind(RewardVariable::getGoalValue, RewardVariable::setGoalValue);
        }
        
        private void setNumberFieldVisibility() {
            if (conditionType.getValue() != null) {
                conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames+" not-none");
            } else {
                conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            }
            goalValueBinding.setAsRequiredEnabled(conditionType.getValue() != null);
            goalField.setVisible(conditionType.getValue() != null);
            goalField.setEnabled(conditionType.getValue() != null);
        }

        @Override
        public RewardVariable getValue() {
            return binder.getBean();
        }

        @Override
        protected void setPresentationValue(RewardVariable newPresentationValue) {
            binder.setBean(newPresentationValue);
            goalSpan.setText(newPresentationValue.getGoalValue() == null ? "" : newPresentationValue.getGoalValue().toString());
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
            return !binder.isValid();
        }
        
        @Override
        public void setReadOnly(boolean readOnly) {
            goalSpan.setVisible(readOnly);
            goalField.setVisible(!readOnly);
        }
    }
}