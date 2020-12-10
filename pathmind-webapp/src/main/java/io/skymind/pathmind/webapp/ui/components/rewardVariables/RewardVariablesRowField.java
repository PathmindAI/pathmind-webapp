package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import static io.skymind.pathmind.webapp.ui.utils.UIConstants.DEFAULT_SELECTED_METRICS_FOR_CHART;

public class RewardVariablesRowField extends HorizontalLayout {

    private Span rewardVariableNameSpan;

    private NumberField goalField;
    private Select<GoalConditionType> conditionType;
    private HorizontalLayout goalFieldsWrapper;
    private Span goalSpan;

    private Binder<RewardVariable> binder;
    private Binder.Binding<RewardVariable, Double> goalValueBinding;
    private String goalOperatorSelectThemeNames = "goals small align-center";
    private Command goalFieldValueChangeHandler;

    private RewardVariable rewardVariable;
    // This is really only used to prevent eventbus updates for reward variables that are already set to show.
    private boolean isShow = true;

    private Supplier<Optional<UI>> getUISupplier;

    protected RewardVariablesRowField(Supplier<Optional<UI>> getUISupplier, RewardVariable rv, Command goalFieldValueChangeHandler, Boolean actAsMultiSelect, RewardVariablesTable rewardVariablesTable) {
        this.getUISupplier = getUISupplier;
        this.rewardVariable = rv;
        this.goalFieldValueChangeHandler = goalFieldValueChangeHandler;
        setAlignItems(Alignment.BASELINE);
        rewardVariableNameSpan = LabelFactory.createLabel(rv.getName(), "reward-variable-name");
        if (actAsMultiSelect) {
            String clickedAttribute = "chosen";
            if (rv.getArrayIndex() < DEFAULT_SELECTED_METRICS_FOR_CHART) {
                rewardVariableNameSpan.getElement().setAttribute(clickedAttribute, true);
                rewardVariablesTable.setNumberOfSelectedRewardVariables(DEFAULT_SELECTED_METRICS_FOR_CHART);
            }

            rewardVariableNameSpan.addClickListener(event -> {
                Element spanElement = event.getSource().getElement();
                int numberOfSelectedRewardVariables = rewardVariablesTable.getNumberOfSelectedRewardVariables();
                if (spanElement.hasAttribute(clickedAttribute)) {
                    spanElement.removeAttribute(clickedAttribute);
                    isShow = false;
                    rewardVariablesTable.setNumberOfSelectedRewardVariables(numberOfSelectedRewardVariables-1);
                    EventBus.post(new RewardVariableSelectedViewBusEvent(rewardVariable, false));
                } else {
                    if (numberOfSelectedRewardVariables < 2) {
                        spanElement.setAttribute(clickedAttribute, true);
                        isShow = true;
                        rewardVariablesTable.setNumberOfSelectedRewardVariables(numberOfSelectedRewardVariables+1);
                        EventBus.post(new RewardVariableSelectedViewBusEvent(rewardVariable, true));
                    }
                }
            });
        }

        conditionType = new Select<>();
        conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
        conditionType.setItemLabelGenerator(type -> type != null ? type.toString() : "None");
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
        goalField.addValueChangeListener(event -> goalFieldValueChangeHandler.execute());

        String goalDisplayText = rv.getGoalConditionType() == null ? "—" : String.format(rv.getGoalConditionTypeEnum().toString() + rv.getGoalValue());
        goalSpan = LabelFactory.createLabel(goalDisplayText, "goal-display-span");

        goalFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, goalField, goalSpan);
        goalFieldsWrapper.addClassName("goal-fields-wrapper");
        goalFieldsWrapper.setVisible(false);
        GuiUtils.removeMarginsPaddingAndSpacing(goalFieldsWrapper);

        add(rewardVariableNameSpan, goalFieldsWrapper, goalSpan);
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
        goalFieldValueChangeHandler.execute();
    }

    public boolean isValid() {
        return binder.validate().isOk();
    }

    public void setEditable(boolean editable) {
        goalFieldsWrapper.setVisible(editable);
        goalSpan.setVisible(!editable);
    }

    public Span getRewardVariableSpan() {
        return rewardVariableNameSpan;
    }

    public RewardVariable getRewardVariable() {
        return rewardVariable;
    }

    public boolean isShow() {
        return isShow;
    }
}
