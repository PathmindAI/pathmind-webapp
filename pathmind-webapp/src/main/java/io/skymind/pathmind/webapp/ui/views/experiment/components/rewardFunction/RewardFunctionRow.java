package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardFunctionRow extends HorizontalLayout {

    private Span rewardVariableNameSpan;

    private NumberField goalField;
    private Select<GoalConditionType> conditionType;
    private HorizontalLayout goalFieldsWrapper;

    private Binder<RewardVariable> binder;
    private Binder.Binding<RewardVariable, Double> goalValueBinding;
    private String goalOperatorSelectThemeNames = "goals small align-center";

    private RewardVariable rewardVariable;

    protected RewardFunctionRow(RewardVariable rv) {
        this.rewardVariable = rv;
        setAlignItems(Alignment.CENTER);
        rewardVariableNameSpan = LabelFactory.createLabel(rewardVariable.getName(), "reward-variable-name");

        conditionType = new Select<>();
        conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
        conditionType.setItemLabelGenerator(type -> type != null ? type.getRewardFunctionComponent().getComment() : "None");
        // The item label generator did not add "None" to the dropdown
        // It only shows if the empty item is selected
        conditionType.setEmptySelectionAllowed(true);
        // This is for the item label on the dropdown
        conditionType.setEmptySelectionCaption("None");
        conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        conditionType.addValueChangeListener(event -> setGoalFieldVisibility());

        goalField = new NumberField();
        goalField.addClassName("goal-field");
        goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        goalField.addValueChangeListener(event -> {});

        goalFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, goalField);
        goalFieldsWrapper.addClassName("goal-fields-wrapper");
        GuiUtils.removeMarginsPaddingAndSpacing(goalFieldsWrapper);

        add(rewardVariableNameSpan, goalFieldsWrapper);
        setWidthFull();
        GuiUtils.removeMarginsPaddingAndSpacing(this);
        initBinder(rv);
        setGoalFieldVisibility();
    }

    private void initBinder(RewardVariable rv) {
        binder = new Binder<>();
        binder.bind(conditionType, RewardVariable::getGoalConditionTypeEnum, RewardVariable::setGoalConditionTypeEnum);
        goalValueBinding = binder.forField(goalField).asRequired("Enter a goal value").bind(RewardVariable::getGoalValue, RewardVariable::setGoalValue);
        binder.setBean(rv);
    }

    private void setGoalFieldVisibility() {
        if (conditionType.getValue() != null) {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames + " not-none");
        } else {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        }
        goalValueBinding.setAsRequiredEnabled(conditionType.getValue() != null);
        goalField.setVisible(conditionType.getValue() != null);
        goalField.setEnabled(conditionType.getValue() != null);
    }

    public boolean isValid() {
        return binder.validate().isOk();
    }

    public void setEditable(boolean editable) {
        goalFieldsWrapper.setVisible(editable);
    }

    public RewardVariable getRewardVariable() {
        return rewardVariable;
    }

    public void setRewardVariable(RewardVariable rewardVariable) {
        this.rewardVariable = rewardVariable;
    }
}
