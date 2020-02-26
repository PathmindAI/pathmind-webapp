package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavbar extends VerticalLayout
{
	private static final String CURRENT = "current";

	private HorizontalLayout oldRow;

	public ExperimentsNavbar(ExperimentDAO experimentDAO, List<Experiment> experiments, Experiment currentExperiment, long modelId, Consumer<Experiment> selectExperimentConsumer)
	{
		VerticalLayout rowsWrapper = new VerticalLayout();
		rowsWrapper.addClassName("experiments-navbar-items");
		rowsWrapper.setPadding(false);
		rowsWrapper.setSpacing(false);

        for (final Experiment experiment : experiments) {
			if (!ExperimentUtils.isDraftRunType(experiment)) {
				Boolean isCurrentExperiment = (experiment.getId() == currentExperiment.getId());
				RunStatus overallExperimentStatus = getRunsStatus(experiment);
				rowsWrapper.add(createRow(experiment, overallExperimentStatus, isCurrentExperiment, selectExperimentConsumer));
			}
        }

		setPadding(false);
		setSpacing(false);
		add(new NewExperimentButton(experimentDAO, modelId));
		add(rowsWrapper);
		addClassName("experiments-navbar");
	}

	private RunStatus getRunsStatus(Experiment experiment) {
		// Discovery Run and Full Run will be combined, 
		// so there's no need to distinguish between them
		return Collections.max(experiment.getRuns(), Comparator.comparingInt(Run::getStatus)).getStatusEnum();
	}

	private HorizontalLayout createRow(Experiment experiment, RunStatus overallExperimentStatus, Boolean isCurrentExperiment, Consumer<Experiment> selectExperimentConsumer) {
		HorizontalLayout newRow = new HorizontalLayout();
		newRow.add(createStatusIcon(overallExperimentStatus));
		newRow.add(createExperimentName(experiment.getName()));
		newRow.addClickListener(event -> getUI().ifPresent(ui -> handleRowClicked(experiment, selectExperimentConsumer, newRow)));
		newRow.addClassName("experiment-navbar-item");
		newRow.setSpacing(false);
		if (isCurrentExperiment) {
			oldRow = newRow;
			newRow.addClassName(CURRENT);
		}
		return newRow;
	}

	private void handleRowClicked(Experiment experiment, Consumer<Experiment> selectExperimentConsumer, HorizontalLayout newRow) {
		newRow.addClassName(CURRENT);
		oldRow.removeClassName(CURRENT);
		selectExperimentConsumer.accept(experiment);
		oldRow = newRow;
	}

	// TODO: +error, stopped status icons
	private Component createStatusIcon(RunStatus status) {
		if (status.getValue() <= RunStatus.Running.getValue()) {
			Div loadingSpinner = new Div();
			loadingSpinner.addClassName("icon-loading-spinner");
			return loadingSpinner;
		} else if (status == RunStatus.Completed) {
			return new Icon(VaadinIcon.COMMENTS.CHECK_CIRCLE);
		}
		return new Span("no icon yet");
	}

	private Div createExperimentName(String experimentNumber) {
		Div experimentNameWrapper = new Div();
		experimentNameWrapper.add(new Paragraph(experimentNumber));
		experimentNameWrapper.add(new Paragraph("Experiment"));
		experimentNameWrapper.addClassName("experiment-name");
		return experimentNameWrapper;
	}
}