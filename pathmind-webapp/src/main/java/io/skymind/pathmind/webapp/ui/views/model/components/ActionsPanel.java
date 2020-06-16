package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.NO_TOP_MARGIN_LABEL;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Action;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class ActionsPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();
    private ActionsTable actionsTable;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));

	private Button draftButton = new Button("Save Draft", new Icon(VaadinIcon.FILE));

	public ActionsPanel()
	{
		setupForm();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		draftButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		HorizontalLayout actionNamesLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
				LabelFactory.createLabel("Action Names", NO_TOP_MARGIN_LABEL),
				draftButton);
		actionNamesLine.getStyle().set("align-items", "center");

		add(actionNamesLine,
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
		formPanel.add(getActionsPanel());
		formPanel.setPadding(false);
	}

    public void setupActionsTable(int numberOfPossibleActions, List<Action> actions) {
	    actionsTable = new ActionsTable();
	    actionsTable.setNumberOfItems(numberOfPossibleActions);
        actionsTable.setValue(actions);
	    formPanel.add(actionsTable);
    }

	private Component getActionsPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Let’s give each action a name", BOLD_LABEL));
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
	
    public List<Action> getActions(){
        return actionsTable.getValue();
    }

	public boolean isInputValueValid() {
        return actionsTable == null || !actionsTable.isInvalid();
	}
}
