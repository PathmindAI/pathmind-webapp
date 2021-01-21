package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.action.SimulationRewardVariableSelectedAction;

public class RewardVariablesRowField extends HorizontalLayout {

    private static final String CLICKED_ATTRIBUTE = "chosen";

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

    private ExperimentView experimentView;
    private boolean isSelected = false;

    protected RewardVariablesRowField(RewardVariable rv, Command goalFieldValueChangeHandler, ExperimentView experimentView, Boolean actAsMultiSelect) {
        this.rewardVariable = rv;
        this.goalFieldValueChangeHandler = goalFieldValueChangeHandler;
        this.experimentView = experimentView;
        setAlignItems(Alignment.BASELINE);
        rewardVariableNameSpan = LabelFactory.createLabel(rewardVariable.getName(), "reward-variable-name");

        if (actAsMultiSelect) {
            setupMultiSelect();
        }

        conditionType = new Select<>();
        conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
        conditionType.setItemLabelGenerator(type -> type != null ? type.getRewardFunctionComponent().getComment() : "None");
        // The item label generator did not add "None" to the dropdown
        // It only shows if the empty item is selected
        conditionType.setEmptySelectionAllowed(true);
        // This is for the item label on the dropdown
        conditionType.setEmptySelectionCaption("None");
        conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
        // conditionType.addValueChangeListener(event -> setGoalFieldVisibility());

        goalField = new NumberField();
        goalField.addClassName("goal-field");
        goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        goalField.addValueChangeListener(event -> goalFieldValueChangeHandler.execute());
        goalField.setVisible(false); // #2541: bring back goal but hide value field
        goalField.setEnabled(false); // #2541: bring back goal but hide value field

        String goalDisplayText = rv.getGoalConditionType() == null ? "—" : String.format(rv.getGoalConditionTypeEnum().getRewardFunctionComponent().getComment());
        goalSpan = LabelFactory.createLabel(goalDisplayText, "goal-display-span");

        goalFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, goalField, goalSpan);
        goalFieldsWrapper.addClassName("goal-fields-wrapper");
        goalFieldsWrapper.setVisible(false);
        GuiUtils.removeMarginsPaddingAndSpacing(goalFieldsWrapper);

        add(rewardVariableNameSpan, goalFieldsWrapper, goalSpan);
        setWidthFull();
        GuiUtils.removeMarginsPaddingAndSpacing(this);
        initBinder(rv);
        // setGoalFieldVisibility();
    }

    private void setupMultiSelect() {
        rewardVariableNameSpan.addClickListener(event ->
                SimulationRewardVariableSelectedAction.selectRewardVariable(rewardVariable, this, experimentView));
    }

    protected void setSelected(boolean selected) {
        isSelected = selected;
        if(isSelected) {
            rewardVariableNameSpan.getElement().setAttribute(CLICKED_ATTRIBUTE, true);
        } else {
            rewardVariableNameSpan.getElement().removeAttribute(CLICKED_ATTRIBUTE);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    private void initBinder(RewardVariable rv) {
        binder = new Binder<>();
        binder.bind(conditionType, RewardVariable::getGoalConditionTypeEnum, RewardVariable::setGoalConditionTypeEnum);
        // goalValueBinding = binder.forField(goalField).asRequired("Enter a goal value").bind(RewardVariable::getGoalValue, RewardVariable::setGoalValue);
        binder.setBean(rv);
    }

    // For #2541, the value field is not needed, but it may be brought back later on
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

    public RewardVariable getRewardVariable() {
        return rewardVariable;
    }
}
