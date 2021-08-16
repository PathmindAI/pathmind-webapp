package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends VerticalLayout implements ExperimentComponent {

    private static final String DISABLE_SELECTION_CLASS_NAME = "disable-selection";

    private List<RewardVariablesRowField> rewardVariableNameFields = new ArrayList<>();
    private VerticalLayout container;
    private Boolean actAsMultiSelect = false;
    // Yes the experiment can be had from the ExperimentView but that is more for the actions for now.
    private ExperimentView experimentView;
    private Experiment experiment;

    /**
     * This constructor is used by the ExperimentView and the selection logic.
     */
    public RewardVariablesTable(ExperimentView experimentView) {
        this();
        this.experimentView = experimentView;
    }

    public RewardVariablesTable() {
        setPadding(false);
        setSpacing(false);
        container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("reward-variables-table");

        add(container);
    }

    public void setCodeEditorMode() {
        setClassName("with-container-border");
    }

    public void setCompactMode() {
        container.addClassName("compact");
    }

    public void setSelectMode() {
        actAsMultiSelect = true;
    }

    /**
     * By the default the table is readonly, as there is only a single case it's editable
     */
    public void makeEditable() {
        rewardVariableNameFields.forEach(row -> row.setEditable(true));
    }

    public void setRewardVariables(List<RewardVariable> rewardVariables) {
        if (rewardVariableNameFields.isEmpty()) {
            HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("Metric"), new Span("Goal"));
            headerRow.addClassName("header-row");
            GuiUtils.removeMarginsPaddingAndSpacing(headerRow);
            container.add(headerRow);
        }

        Collections.sort(rewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
        rewardVariables.forEach(rewardVariable -> {
            if (rewardVariableNameFields.size() < rewardVariables.size()) {
                RewardVariablesRowField row = new RewardVariablesRowField(rewardVariable, experimentView, actAsMultiSelect);
                    container.add(row);
                    rewardVariableNameFields.add(row);
            } else {
                RewardVariablesRowField row = rewardVariableNameFields.get(rewardVariable.getArrayIndex());
                row.setSelected(false);
                row.setRewardVariable(rewardVariable);
            }
        });
    }

    public boolean canSaveChanges() {
        return rewardVariableNameFields.stream().allMatch(row -> row.isValid());
    }

    public void updateSelectionClassForComponent() {
        if (experiment.getSelectedRewardVariables().size() >= RewardVariable.MAX_SELECTED_REWARD_VARIABLES) {
            container.addClassName(DISABLE_SELECTION_CLASS_NAME);
        } else {
            container.removeClassName(DISABLE_SELECTION_CLASS_NAME);
        }
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        setRewardVariables(experiment.getRewardVariables());
        if (actAsMultiSelect) {
            selectSelectedRewardVariables(experiment);
            updateSelectionClassForComponent();
        }
    }

    private void selectSelectedRewardVariables(Experiment experiment) {
        rewardVariableNameFields.stream()
                .filter(rewardVariablesRowField -> experiment.getSelectedRewardVariables().contains(rewardVariablesRowField.getRewardVariable()))
                .forEach(rewardVariablesRowField ->  rewardVariablesRowField.setSelected(true));
    }
}
