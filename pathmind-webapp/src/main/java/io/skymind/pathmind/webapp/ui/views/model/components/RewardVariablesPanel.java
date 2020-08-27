package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardVariablesPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();
	private RewardVariablesTable rewardVariablesTable;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));

	public RewardVariablesPanel()
	{
		setupForm();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout rewardVariablesNameLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
				LabelFactory.createLabel("Reward Variable Names", NO_TOP_MARGIN_LABEL));
		rewardVariablesNameLine.getStyle().set("align-items", "center");

		add(rewardVariablesNameLine,
				GuiUtils.getFullWidthHr(),
				formPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton));

		setWidthFull();
		setPadding(false);
		setSpacing(false);
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	private void setupForm() {
        rewardVariablesTable = new RewardVariablesTable();
		formPanel.add(new Paragraph("You have created a function to gather reward variables in your simulation. Here is the list of reward variables we extracted from your simulation:"));
		formPanel.setPadding(false);
		formPanel.add(rewardVariablesTable);
	}

	public void setupRewardVariablesTable(int rewardVariablesCount, List<RewardVariable> rewardVariables) {
	    rewardVariablesTable.setRewardVariables(rewardVariables);
	}
}
