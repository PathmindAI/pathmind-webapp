package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;

import java.util.List;

// TODO: CSS styling for the side nav
public class ExperimentsNavbar extends VerticalLayout {
	@Autowired
	private ExperimentDAO experimentDAO;
	
	public ExperimentsNavbar(List<Experiment> experiments, long modelId) {
		add(new NewExperimentButton(experimentDAO, modelId));

		experiments.forEach(experiment -> {
			add(createRow(experiment));
		});

		setPadding(false);
	}

	private HorizontalLayout createRow(Experiment experiment) {
		// TODO: row should bring user to the according experiment page on click
		HorizontalLayout newRow = new HorizontalLayout();
		newRow.add(new Span("Status"));
		newRow.add(new Span("Experiment "+experiment.getName()));
		return newRow;
	}
	
	// TODO: function to get experiment status for each row

	// TODO: function to create Status icon according to status

	// TODO: function to highlight current experiment row
}