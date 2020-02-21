package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;

import java.util.List;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavbar extends VerticalLayout {

	public ExperimentsNavbar(ExperimentDAO experimentDAO, List<Experiment> experiments, Experiment currentExperiment, long modelId) {

		VerticalLayout rowsWrapper = new VerticalLayout();
		rowsWrapper.addClassName("experiments-navbar-items");
		rowsWrapper.setPadding(false);
		rowsWrapper.setSpacing(false);

        for (final Experiment experiment : experiments) {
			if (!ExperimentUtils.isDraftRunType(experiment)) {
				Boolean isCurrentExperiment = (experiment.getId() == currentExperiment.getId());
				RunStatus overallExperimentStatus = ExperimentUtils.getTrainingStatus(experiment);
				rowsWrapper.add(createRow(experiment, overallExperimentStatus, isCurrentExperiment));
			}
        }

		setPadding(false);
		setSpacing(false);
		add(new NewExperimentButton(experimentDAO, modelId));
		add(rowsWrapper);
		addClassName("experiments-navbar");
	}

	private HorizontalLayout createRow(Experiment experiment, RunStatus overallExperimentStatus, Boolean isCurrentExperiment) {
		String experimentId = Long.toString(experiment.getId());
		HorizontalLayout newRow = new HorizontalLayout();
		newRow.add(createStatusIcon(overallExperimentStatus));
		newRow.add(createExperimentName(experiment.getName()));
		newRow.addClickListener(event -> getUI().ifPresent(ui -> 
				ui.navigate(ExperimentView.class, experimentId)));
		newRow.addClassName("experiment-navbar-item");
		newRow.setSpacing(false);
		if (isCurrentExperiment) {
			newRow.addClassName("current");
		}
		return newRow;
	}

	private Component createStatusIcon(RunStatus status) {
		if (status.getValue() <= RunStatus.Running.getValue()) {
			Div loadingSpinner = new Div();
			loadingSpinner.addClassName("icon-loading-spinner");
			return loadingSpinner;
		} else if (status == RunStatus.Completed) {
			return new Icon(VaadinIcon.COMMENTS.CHECK_CIRCLE);
		}
		return new Icon(VaadinIcon.EXCLAMATION_CIRCLE_O);
	}

	private Div createExperimentName(String experimentNumber) {
		Div experimentNameWrapper = new Div();
		experimentNameWrapper.add(new Paragraph(experimentNumber));
		experimentNameWrapper.add(new Paragraph("Experiment"));
		experimentNameWrapper.addClassName("experiment-name");
		return experimentNameWrapper;
	}
}