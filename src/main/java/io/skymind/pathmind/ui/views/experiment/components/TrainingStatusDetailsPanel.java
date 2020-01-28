package io.skymind.pathmind.ui.views.experiment.components;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.vaadin.flow.component.progressbar.ProgressBar;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.db.dao.PolicyDAO;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.constant.RunConstants;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.stereotype.Component;

import static io.skymind.pathmind.constants.RunStatus.*;
import static io.skymind.pathmind.constants.RunType.FullRun;
import static io.skymind.pathmind.services.training.constant.RunConstants.*;

@Component
public class TrainingStatusDetailsPanel extends VerticalLayout {
	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runProgressLabel = new Label();
	private Label runTypeLabel = new Label();
	private Label progressValueLabel = new Label();
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
	private ProgressBar progressBar = new ProgressBar(0, 100);
	private VerticalLayout progressRow = new VerticalLayout(progressBar, progressValueLabel);

	public TrainingStatusDetailsPanel() {
		VerticalLayout wrapper = new VerticalLayout();
		var statusRow = createHorizontalLayout("Status", statusLabel);
		var runProgressRow = createHorizontalLayout("", runProgressLabel);
		var runTypeRow = createHorizontalLayout("Run Type", runTypeLabel);
		var elapsedTimeRow = createHorizontalLayout("Elapsed", elapsedTimeLabel);

		styleProgressLayout();

		wrapper.add(statusRow, progressRow, runProgressRow, runTypeRow, elapsedTimeRow);
		wrapper.getStyle().set("padding-top", "10px");
		wrapper.setWidthFull();

		add(wrapper);
	}

	private void styleProgressLayout() {
		progressBar.getStyle().set("margin", "0px").set("max-width", "200px");
		progressValueLabel.getStyle().set("margin-top", "6px");
		progressRow.getStyle().set("margin-top" ,"24px").set("margin-left", "110px");
		progressRow.setPadding(false);
	}

	private HorizontalLayout createHorizontalLayout(String labelTitle, Label valueLabel) {
		final var elementLabel = getElementLabel(labelTitle);
		final var horizontalLayout = new HorizontalLayout(elementLabel, valueLabel);
		horizontalLayout.getStyle().set("margin-top" ,"0px");
		valueLabel.getStyle().set("margin-top", "0px");
		horizontalLayout.setPadding(false);
		return horizontalLayout;
	}

	private Label getElementLabel(String label) {
		Label fieldLabel = new Label(label + ":");
		fieldLabel.getStyle()
				.set("font-weight", "bold")
				.set("margin-top", "0px")
				.set("min-width", "90px")
				.set("text-align", "right");
		return fieldLabel;
	}

	public void updateTrainingDetailsPanel(Experiment experiment) {
		final var trainingStatus = ExperimentUtils.getTrainingStatus(experiment);
		statusLabel.setText(trainingStatus.toString());

		final var runType = ExperimentUtils.getTrainingType(experiment);
		runTypeLabel.setText(runType.toString());

		updateElapsedTimer(experiment, trainingStatus);
		DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
			final var trainingCompletedTime = ExperimentUtils.getTrainingCompletedTime(experiment);
			final var formattedTrainingCompletedTime = DateAndTimeUtils.formatDateAndTimeShortFormatter(trainingCompletedTime, userTimeZone);
			runProgressLabel.setText(formattedTrainingCompletedTime);
		});
		updateProgressRow(experiment, trainingStatus, runType);
	}

	private void updateProgressRow(Experiment experiment, RunStatus trainingStatus, RunType runType) {
		if(trainingStatus == Running) {
			progressRow.setVisible(true);
			updateProgressBar(experiment, runType);
		} else {
			progressRow.setVisible(false);
		}
	}

	// TODO (KW): 25.01.2020 add full run scenario
	private void updateProgressBar(Experiment experiment, RunType runType) {
		final var totalIterations = (double) RunUtils.getNumberOfTrainingIterationsForRunType(runType);
		final Integer iterationsProcessed = ExperimentUtils.getNumberOfProcessedIterations(experiment);

		final var progress = (iterationsProcessed / totalIterations) * 100;
		if (progress > 0 && progress <= 100) {
			final var estimatedTime = ExperimentUtils.getEstimatedTrainingTime(experiment, progress);
			final var formattedEstimatedTime = DateAndTimeUtils.formatDurationTime((long) estimatedTime);
			final var progressValue = formatProgressLabel(progress, formattedEstimatedTime);
			progressValueLabel.setText(progressValue);
			progressBar.setValue(progress);
		}
	}

	// TODO (KW): 28.01.2020 javadoc
	private String formatProgressLabel(double progress, String formattedEstimatedTime) {
		return String.format("%.0f %% (ETA: %s)", progress, formattedEstimatedTime);
	}

	private void updateElapsedTimer(Experiment experiment,  RunStatus trainingStatus) {
		ExperimentUtils.getTrainingStartedDate(experiment).ifPresent(time -> {
			// TODO (KW): 28.01.2020 fix - it should take stopped_at if training is over
			final var timeElapsed = Duration.between(time, LocalDateTime.now()).toSeconds();
			elapsedTimeLabel.updateTimer(timeElapsed, isRunning(trainingStatus));
		});
	}
}