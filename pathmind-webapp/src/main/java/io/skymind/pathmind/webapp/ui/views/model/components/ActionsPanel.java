package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

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

	public ActionsPanel()
	{
		setupForm();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout actionNamesLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
				LabelFactory.createLabel("Action Names", NO_TOP_MARGIN_LABEL));
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

	private void setupForm() {
	    actionsTable = new ActionsTable();
		formPanel.add(getActionsPanel());
		formPanel.setPadding(false);
		formPanel.add(actionsTable);
	}

    public void setupActionsTable(int numberOfPossibleActions, List<Action> actions) {
	    actionsTable.setNumberOfItems(numberOfPossibleActions);
        actionsTable.setValue(actions);
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
        return !actionsTable.isInvalid();
	}
}
