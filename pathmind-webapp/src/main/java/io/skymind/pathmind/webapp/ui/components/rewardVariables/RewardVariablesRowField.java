package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
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

    private Select<GoalConditionType> conditionType;
    private HorizontalLayout goalFieldsWrapper;
    private Span goalSpan;

    private Binder<RewardVariable> binder;
    private String goalOperatorSelectThemeNames = "goals small align-center";

    private RewardVariable rewardVariable;

    private ExperimentView experimentView;
    private boolean isSelected = false;

    protected RewardVariablesRowField(RewardVariable rv, ExperimentView experimentView, Boolean actAsMultiSelect) {
        this.rewardVariable = rv;
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

        String goalDisplayText = rv.getGoalConditionType() == null ? "—" : String.format(rv.getGoalConditionTypeEnum().getRewardFunctionComponent().getComment());
        goalSpan = LabelFactory.createLabel(goalDisplayText, "goal-display-span");

        goalFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, goalSpan);
        goalFieldsWrapper.addClassName("goal-fields-wrapper");
        goalFieldsWrapper.setVisible(false);
        GuiUtils.removeMarginsPaddingAndSpacing(goalFieldsWrapper);

        add(rewardVariableNameSpan, goalFieldsWrapper, goalSpan);
        setWidthFull();
        GuiUtils.removeMarginsPaddingAndSpacing(this);
        initBinder(rv);
    }

    private void setupMultiSelect() {
        rewardVariableNameSpan.addClickListener(event ->
                SimulationRewardVariableSelectedAction.selectRewardVariable(rewardVariable, this, experimentView));
    }

    protected void setSelected(boolean selected) {
        isSelected = selected;
        if (isSelected) {
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
        binder.setBean(rv);
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

    public void setRewardVariable(RewardVariable rewardVariable) {
        this.rewardVariable = rewardVariable;
    }
}
