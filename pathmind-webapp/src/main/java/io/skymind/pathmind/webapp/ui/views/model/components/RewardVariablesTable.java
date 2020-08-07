package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
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

    public void setRewardVariables(List<RewardVariable> rewardVariables) {
        container.removeAll();
        rewardVariableNameFields.clear();
        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("#"), new Span("Variable Name"));

        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

        container.add(headerRow);

        rewardVariables.forEach(rv -> container.add(createRow(rv)));
    }

    public void setIsReadOnly(boolean readOnly) {
        container.getElement().setAttribute("readonly", readOnly);
        container.getChildren().filter(c -> RowField.class.isInstance(c)).map(c -> RowField.class.cast(c)).forEach(rf -> rf.setReadOnly(readOnly));
    }

    private RowField createRow(RewardVariable rv) {
        RowField rewardVariableNameField = new RowField(rv);
        rewardVariableNameFields.add(rewardVariableNameField);
        return rewardVariableNameField;
    }

    private static class RowField extends HorizontalLayout {

        private final int rowNumber;

        private final TextField rewardVariableNameField;
        private final Span rewardVariableNameSpan;

        private RowField(RewardVariable rv) {
            rowNumber = rv.getArrayIndex();
            rewardVariableNameSpan = LabelFactory.createLabel(rv.getName(), ("variable-color-"+rowNumber%10));
            rewardVariableNameField = new TextField();
            rewardVariableNameField.setValue(String.format("%s (%s)", rv.getName(), rv.getDataType()));
            rewardVariableNameField.addClassName("reward-variable-name-field");
            rewardVariableNameField.setReadOnly(true);
            add(new Span("" + rowNumber), rewardVariableNameSpan, rewardVariableNameField);
            setWidthFull();
            GuiUtils.removeMarginsPaddingAndSpacing(this);
            setReadOnly(false);
        }
        
        private void setReadOnly(boolean readOnly){
            rewardVariableNameSpan.setVisible(readOnly);
            rewardVariableNameField.setVisible(!readOnly);
        }
    }
}
