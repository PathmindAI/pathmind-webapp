package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.Data;
import io.skymind.pathmind.shared.data.RewardTerm;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import org.apache.commons.lang3.ObjectUtils;

public class RewardFunctionRow extends CustomField<RewardTerm> implements RewardTermRow {

    private static final String goalOperatorSelectThemeNames = "small align-center";

    private final HorizontalLayout weightFieldsWrapper;
    private final Select<RewardVariable> rewardVariableSelect = new Select<>();
    private final Select<GoalConditionType> conditionType;
    private final NumberField weightField;
    private final List<RewardVariable> rewardVariables;
    private RewardTerm rewardTerm;
    private Binder<RewardTerm> binder;
    private final Command changeHandler;

    protected RewardFunctionRow(List<RewardVariable> rvars, Command changeHandler) {
        this.rewardVariables = rvars;
        this.changeHandler = changeHandler;
        rewardVariableSelect.setPlaceholder("Choose a reward variable");
        rewardVariableSelect.setItems(rvars);
        rewardVariableSelect.setItemLabelGenerator(Data::getName);
        rewardVariableSelect.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        rewardVariableSelect.addValueChangeListener(event -> {
            this.rewardTerm.setRewardVariableIndex(event.getValue().getArrayIndex());
        });

        conditionType = new Select<>();
        conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
        conditionType.setItemLabelGenerator(type -> type != null ? type.getRewardFunctionComponent().getComment() : "");
        conditionType.setPlaceholder("Choose goal");
        conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        conditionType.addValueChangeListener(event -> {
            setGoalFieldVisibility();
        });

        weightField = new NumberField();
        weightField.setPlaceholder("Weight");
        weightField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        weightField.setHasControls(true);

        weightFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, new Span("x"), weightField);
        weightFieldsWrapper.addClassName("goal-fields-wrapper");
        GuiUtils.removeMarginsPaddingAndSpacing(weightFieldsWrapper);

        HorizontalLayout wrapper = WrapperUtils.wrapSizeFullBetweenHorizontal(
            rewardVariableSelect, weightFieldsWrapper
        );
        wrapper.setAlignItems(Alignment.CENTER);

        add(wrapper);
        setWidthFull();
        setGoalFieldVisibility();
    }

    private void initBinder() {
        binder = new Binder<>();
        binder.bind(conditionType, RewardTerm::getGoalCondition, RewardTerm::setGoalCondition);
        binder.bind(weightField, RewardTerm::getWeight, RewardTerm::setWeight);
        binder.addValueChangeListener(event -> changeHandler.execute());
        binder.setBean(rewardTerm);
    }
    
    private void setGoalFieldVisibility() {
        String ignoreClassName = "ignore";
        if (conditionType.getValue() != null) {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames + " not-none");
            if (weightFieldsWrapper.hasClassName(ignoreClassName)) {
                weightFieldsWrapper.removeClassName(ignoreClassName);
            }
        } else {
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            weightFieldsWrapper.addClassName(ignoreClassName);
        }
        weightField.setEnabled(conditionType.getValue() != null);
    }

    private void setRewardVariable(RewardVariable rewardVariable) {
        this.rewardVariableSelect.setValue(rewardVariable);
    }

    @Override
    public Component asComponent() {
        return this;
    }

    @Override
    protected RewardTerm generateModelValue() {
        return rewardTerm;
    }

    @Override
    protected void setPresentationValue(RewardTerm rewardTerm) {
        this.rewardTerm = rewardTerm;
        if (binder == null) {
            initBinder();
        }
        if (rewardTerm.getRewardVariableIndex() != null) {
            setRewardVariable(rewardVariables.get(rewardTerm.getRewardVariableIndex()));
        }
    }

    @Override
    public Optional<RewardTerm> convertToValueIfValid(int index) {
        final Integer rewardVariableIndex = rewardTerm.getRewardVariableIndex();
        final GoalConditionType conditionType = rewardTerm.getGoalCondition();
        final Double weight = rewardTerm.getWeight();
        if (ObjectUtils.allNotNull(rewardVariableIndex, conditionType, weight)) {
            return Optional.of(new RewardTerm(index, weight, rewardVariableIndex, conditionType));
        }
        return Optional.empty();
    }
}
