package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.RewardVariable;
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
	    container.getElement().setAttribute("readonly", true);
	    add(container);
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
	
	public void setCodeEditorMode() {
        setClassName("with-container-border");
    }

	private RowField createRow(RewardVariable rv) {
		RowField rewardVariableNameField = new RowField(rv);
        rewardVariableNameFields.add(rewardVariableNameField);
		return rewardVariableNameField;
    }

	private static class RowField extends HorizontalLayout {

		private final int rowNumber;

        private final Span rewardVariableNameSpan;

		private RowField(RewardVariable rv) {
            this.rowNumber = rv.getArrayIndex();
            this.rewardVariableNameSpan = new Span();
            add(new Span("" + rowNumber));
            rewardVariableNameSpan.addClassName("variable-color-"+rowNumber%10);
            rewardVariableNameSpan.setText(rv.getName());
            add(rewardVariableNameSpan);
			setWidthFull();
			GuiUtils.removeMarginsPaddingAndSpacing(this);
		}
	}
}