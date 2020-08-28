package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class ObservationsPanel extends VerticalLayout
{
    private ObservationsTable observationsTable;

	public ObservationsPanel()
	{
	    observationsTable = new ObservationsTable();

		add(LabelFactory.createLabel("Observations", BOLD_LABEL),
            getObservationsPanel());

		setWidthFull();
		setPadding(false);
		setSpacing(false);
	}

    public void setupObservationTable(List<Observation> observations) {
        observationsTable.setObservations(observations);
    }

	private Component getObservationsPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
				new Span("Select observations for this experiment"), 
                observationsTable);
        wrapper.addClassName("observations-panel");
		return wrapper;
	}
	
    public List<Observation> getObservations(){
        return observationsTable.getObservations();
    }
}
