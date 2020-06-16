package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.NO_TOP_MARGIN_LABEL;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
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

	private Button draftButton = new Button("Save Draft", new Icon(VaadinIcon.FILE));

	public RewardVariablesPanel()
	{
		setupForm();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		draftButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		HorizontalLayout rewardVariablesNameLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
				LabelFactory.createLabel("Reward Variable Names", NO_TOP_MARGIN_LABEL),
				draftButton);
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

	public void addSaveDraftClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		draftButton.addClickListener(listener);
	}

	private void setupForm() {
	    rewardVariablesTable = new RewardVariablesTable();
		formPanel.add(new Paragraph("You have created a function to gather reward variables in your simulation. Let’s give them variable names to make it easier to remember what they reference."));
		formPanel.add(getRewardVariablesPanel());
		formPanel.setPadding(false);
		formPanel.add(rewardVariablesTable);
	}

	public void setupRewardVariablesTable(int rewardVariablesCount, List<RewardVariable> rewardVariables) {
		rewardVariablesTable.setVariableSize(Math.max(rewardVariablesCount, rewardVariables.size()));
		rewardVariablesTable.setValue(rewardVariables);
	}

	private Component getRewardVariablesPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Let’s give each variable a name", BOLD_LABEL),
				LabelFactory.createLabel("This will make it easier to understand when you’re creating reward functions."));
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
	
	public List<RewardVariable> getRewardVariables(){
		return rewardVariablesTable.getValue();
	}

	public boolean isInputValueValid() {
        return !rewardVariablesTable.isInvalid();
	}
}
