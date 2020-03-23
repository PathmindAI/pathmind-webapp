package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.NO_TOP_MARGIN_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_SUBTITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.TRUNCATED_LABEL;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardVariablesPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();
	private RewardVariablesTable rewardVariablesTable;

	private Div sectionTitleWrapper;
	private Span projectNameLabel;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));

	public RewardVariablesPanel()
	{
		setupForm();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		sectionTitleWrapper = new Div();
		Span projectText = new Span("Project: ");
		projectText.addClassName(SECTION_TITLE_LABEL);
		projectNameLabel = LabelFactory.createLabel("", SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT, SECTION_SUBTITLE_LABEL);
		sectionTitleWrapper.add(projectText, projectNameLabel);
		sectionTitleWrapper.addClassName(TRUNCATED_LABEL);

		add(sectionTitleWrapper,
				LabelFactory.createLabel("Reward Variable Names", NO_TOP_MARGIN_LABEL),
				GuiUtils.getFullWidthHr(),
				formPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton));

		setWidthFull();
		setClassName("view-section"); // adds the white 'panel' style with rounded corners
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	public void setProjectName(String name) {
		projectNameLabel.setText(name);
	}

	private void setupForm() {
		formPanel.add(new Paragraph("You have created a function to gather reward variables in your simulation. Let’s give them variable names to make it easier to remember what they reference."));
		formPanel.add(getRewardVariablesPanel());
		formPanel.setPadding(false);
	}

	public void setupRewardVariablesTable(int rewardVariablesCount) {
		rewardVariablesTable = new RewardVariablesTable();
		rewardVariablesTable.setVariableSize(rewardVariablesCount);
		formPanel.add(rewardVariablesTable);
	}

	private Component getRewardVariablesPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Let’s give each variable a name", BOLD_LABEL),
				LabelFactory.createLabel("This will make it easier to understand when you’re creating reward functions."));
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
	
	public List<RewardVariable> getRewardVariables(){
		if (rewardVariablesTable == null) {
			return null;
		} else {
			return rewardVariablesTable.getValue();
		}
	}
}
