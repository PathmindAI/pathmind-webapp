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
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardFunctionRow extends HorizontalLayout {

    private NumberField goalField;
    private Select<RewardVariable> rewardVariableSelect;
    private Select<GoalConditionType> conditionType;
    private HorizontalLayout goalFieldsWrapper;

    private Binder<RewardVariable> binder;
    private String goalOperatorSelectThemeNames = "small align-center";

    private RewardVariable rewardVariable;

    protected RewardFunctionRow(List<RewardVariable> rvars) {
        setAlignItems(Alignment.CENTER);
        rewardVariableSelect = new Select<>();
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
        } else {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        }
        if (conditionType.getValue() != null) {
            if (goalFieldsWrapper.hasClassName(ignoreClassName)) {
                goalFieldsWrapper.removeClassName(ignoreClassName);
            }
        } else {
            goalFieldsWrapper.addClassName(ignoreClassName);
        }
        goalField.setEnabled(conditionType.getValue() != null);
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
