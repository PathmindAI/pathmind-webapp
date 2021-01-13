package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import static io.skymind.pathmind.webapp.ui.utils.UIConstants.MAX_SELECTED_METRICS_FOR_CHART;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends VerticalLayout {

    private List<RewardVariablesRowField> rewardVariableNameFields = new ArrayList<>();
    private VerticalLayout container;
    private Command goalFieldValueChangeHandler;
    private Boolean actAsMultiSelect = false;
    private Integer selectedRewardVariables;

    public RewardVariablesTable() {
        this(() -> {
        });
    }

    public RewardVariablesTable(Command goalFieldValueChangeHandler) {
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
            RewardVariablesRowField row = new RewardVariablesRowField(rewardVariable, goalFieldValueChangeHandler, actAsMultiSelect, this);
            container.add(row);
            rewardVariableNameFields.add(row);
        });
    }

    public boolean canSaveChanges() {
        return rewardVariableNameFields.stream().allMatch(row -> row.isValid());
    }

    public int getNumberOfSelectedRewardVariables() {
        return selectedRewardVariables;
    }

    public void setNumberOfSelectedRewardVariables(int num) {
        String disableSelectionClassName = "disable-selection";
        selectedRewardVariables = num;
        if (num >= MAX_SELECTED_METRICS_FOR_CHART) {
            container.addClassName(disableSelectionClassName);
        } else {
            container.removeClassName(disableSelectionClassName);
        }
    }
}
