package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

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

    public void setupObservationTable(Collection<Observation> allObservations, Collection<Observation> selection) {
        observationsTable.setItems(new HashSet<>(allObservations));
        observationsTable.setValue(new HashSet<>(selection));
    }
    
    public Collection<Observation> getSelectedObservations(){
        return observationsTable.getValue();
    }
  
    public void addValueChangeListener(SerializableConsumer<Set<Observation>> listener) {
        observationsTable.addValueChangeListener(evt -> listener.accept(evt.getValue()));
    }
    

	private Component getObservationsPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
				new Span("Select observations for this experiment"), 
                observationsTable);
        wrapper.addClassName("observations-panel");
		return wrapper;
	}
	
    
}
