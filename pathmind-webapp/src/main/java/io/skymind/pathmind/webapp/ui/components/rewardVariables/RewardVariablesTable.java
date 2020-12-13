package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends VerticalLayout implements ExperimentComponent {

    private List<RewardVariablesRowField> rewardVariableNameFields = new ArrayList<>();
    private VerticalLayout container;
    private Command goalFieldValueChangeHandler;
    private Boolean actAsMultiSelect = false;
    private Supplier<Optional<UI>> getUISupplier;
    private Integer selectedRewardVariables;

    public RewardVariablesTable(Supplier<Optional<UI>> getUISupplier) {
        this(getUISupplier, () -> {
        });
    }

    public RewardVariablesTable(Supplier<Optional<UI>> getUISupplier, Command goalFieldValueChangeHandler) {
        this.getUISupplier = getUISupplier;
        this.goalFieldValueChangeHandler = goalFieldValueChangeHandler;
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
        container.removeAll();
        rewardVariableNameFields.clear();
        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("Variable Name"), new Span("Goal"));

        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

        container.add(headerRow);

        Collections.sort(rewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
        rewardVariables.forEach(rewardVariable -> {
            RewardVariablesRowField row = new RewardVariablesRowField(getUISupplier, rewardVariable, goalFieldValueChangeHandler, actAsMultiSelect, this);
            container.add(row);
            rewardVariableNameFields.add(row);
        });
    }

    public boolean canSaveChanges() {
        return rewardVariableNameFields.stream().allMatch(row -> row.isValid());
    }

    public void setNumberOfSelectedRewardVariables(int num) {
        String disableSelectionClassName = "disable-selection";
        selectedRewardVariables = num;
        if (num >= 2) {
            container.addClassName(disableSelectionClassName);
        } else {
            container.removeClassName(disableSelectionClassName);
        }
    }

    public int getNumberOfSelectedRewardVariables() {
        return selectedRewardVariables;
    }


    @Override
    public void setExperiment(Experiment experiment) {
        setRewardVariables(experiment.getRewardVariables());
    }
}
