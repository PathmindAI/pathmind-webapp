package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends VerticalLayout {

	private List<RowField> rewardVariableNameFields = new ArrayList<>();
	private VerticalLayout container;

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

    public void setRewardVariables(List<RewardVariable> rewardVariables) {
        container.removeAll();
        rewardVariableNameFields.clear();
        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("#"), new Span("Variable Name"));

        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

        container.add(headerRow);
        
        Collections.sort(rewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
        rewardVariables.forEach(rv -> container.add(createRow(rv)));
    }

    private RowField createRow(RewardVariable rv) {
        RowField rewardVariableNameField = new RowField(rv);
        rewardVariableNameFields.add(rewardVariableNameField);
        return rewardVariableNameField;
    }

    private static class RowField extends HorizontalLayout {

        private RowField(RewardVariable rv) {
            Span rewardVariableIndexSpan = LabelFactory.createLabel(Integer.toString(rv.getArrayIndex()), "reward-variable-index");
            Span rewardVariableNameSpan = LabelFactory.createLabel(rv.getName(), ("variable-color-"+ (rv.getArrayIndex() % 10)), "reward-variable-name");
            TextField rewardVariableNameField = new TextField();
            rewardVariableNameField.setValue(String.format("%s (%s)", rv.getName(), rv.getDataType()));
            rewardVariableNameField.addClassName("reward-variable-name-field");
            rewardVariableNameField.setReadOnly(true);
            add(rewardVariableIndexSpan, rewardVariableNameSpan, rewardVariableNameField);
            setWidthFull();
            GuiUtils.removeMarginsPaddingAndSpacing(this);
        }
        
    }
}
