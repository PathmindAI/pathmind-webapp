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

import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class ObservationsPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();
    private ObservationsTable observationsTable;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));

	public ObservationsPanel()
	{
		setupForm();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout observationsLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
				LabelFactory.createLabel("Observations", NO_TOP_MARGIN_LABEL));
		observationsLine.getStyle().set("align-items", "center");

		add(observationsLine,
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
	    observationsTable = new ObservationsTable();
		formPanel.add(getObservationsPanel());
		formPanel.setPadding(false);
		formPanel.add(observationsTable);
	}

    public void setupObservationTable(int numberOfObservations, List<Observation> observations) {
        observationsTable.setNumberOfItems(numberOfObservations);
        observationsTable.setValue(observations);
    }

	private Component getObservationsPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Let’s define observations", BOLD_LABEL));
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
	
    public List<Observation> getObservations(){
        return observationsTable.getValue();
    }

	public boolean isInputValueValid() {
        return !observationsTable.isInvalid();
	}
}
