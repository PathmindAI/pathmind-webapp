package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.List;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import lombok.Getter;

public class RewardFunctionRow extends HorizontalLayout {

    private static final String goalOperatorSelectThemeNames = "small align-center";

    @Getter
    private RewardVariable rewardVariable;

    private final HorizontalLayout goalFieldsWrapper;
    private final Select<RewardVariable> rewardVariableSelect = new Select<>();
    private final Select<GoalConditionType> conditionType;
    private final NumberField goalField;

    private Binder<RewardVariable> binder;

    protected RewardFunctionRow(List<RewardVariable> rvars) {
        setAlignItems(Alignment.CENTER);
        rewardVariableSelect.setPlaceholder("Choose a reward variable");
        rewardVariableSelect.setItems(rvars);
        rewardVariableSelect.setItemLabelGenerator(rv -> rv.getName());
        rewardVariableSelect.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        rewardVariableSelect.addValueChangeListener(event -> {
            rewardVariable = event.getValue();
            if (binder != null) {
                binder.removeBean();
            }
            initBinder();
        });

        conditionType = new Select<>();
        conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
        conditionType.setItemLabelGenerator(type -> type != null ? type.getRewardFunctionComponent().getComment() : "");
        conditionType.setPlaceholder("Choose goal");
        conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        conditionType.addValueChangeListener(event -> setGoalFieldVisibility());

        goalField = new NumberField();
        goalField.setPlaceholder("Weight");
        goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        goalField.addValueChangeListener(event -> {});

        goalFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, new Span("x"), goalField);
        goalFieldsWrapper.addClassName("goal-fields-wrapper");
        GuiUtils.removeMarginsPaddingAndSpacing(goalFieldsWrapper);

        add(rewardVariableSelect, goalFieldsWrapper);
        setWidthFull();
        GuiUtils.removeMarginsPaddingAndSpacing(this);
        setGoalFieldVisibility();
    }

    private void initBinder() {
        binder = new Binder<>();
        binder.bind(conditionType, RewardVariable::getGoalConditionTypeEnum, RewardVariable::setGoalConditionTypeEnum);
        binder.setBean(rewardVariable);
    }

    private void setGoalFieldVisibility() {
        String ignoreClassName = "ignore";
        if (conditionType.getValue() != null) {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames + " not-none");
            if (goalFieldsWrapper.hasClassName(ignoreClassName)) {
                goalFieldsWrapper.removeClassName(ignoreClassName);
            }
        } else {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            goalFieldsWrapper.addClassName(ignoreClassName);
        }
        goalField.setEnabled(conditionType.getValue() != null);
    }

    public Double getWeight() {
        return this.goalField.getValue();
    }

    public void setWeight(Double weight) {
        this.goalField.setValue(weight);
    }

    public GoalConditionType getGoalCondition() {
        return this.conditionType.getValue();
    }

    public void setGoalCondition(GoalConditionType goalCondition) {
        this.conditionType.setValue(goalCondition);
    }

    public void setRewardVariable(RewardVariable rewardVariable) {
        this.rewardVariable = rewardVariable;
        this.rewardVariableSelect.setValue(this.rewardVariable);
    }
}
