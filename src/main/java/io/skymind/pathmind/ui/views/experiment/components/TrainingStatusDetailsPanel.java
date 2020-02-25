package io.skymind.pathmind.ui.views.experiment.components;

import static io.skymind.pathmind.constants.RunStatus.Completed;
import static io.skymind.pathmind.constants.RunStatus.Running;
import static io.skymind.pathmind.constants.RunStatus.isRunning;

import java.time.Duration;
import java.time.LocalDateTime;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.services.training.constant.RunConstants;
import io.skymind.pathmind.ui.components.ElapsedTimer;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class TrainingStatusDetailsPanel extends VerticalLayout {
	private Span statusLabel = LabelFactory.createLabel(RunStatus.NotStarted.toString());
	/**
	 * Label for training progress status.
	 * If training is still in progress it shows it's % progress. If training is finished it shows its completed date.
	 */
	private Span progressValueLabel = LabelFactory.createLabel("");
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
	private ProgressBar progressBar = new ProgressBar(0, 100);

	public TrainingStatusDetailsPanel() {
		VerticalLayout progressRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(progressBar, progressValueLabel);
		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
		VerticalLayout statusRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(statusLabel, progressRow);
		formLayout.addFormItem(statusRow, "Status :").setClassName("training-status");
		formLayout.addFormItem(elapsedTimeLabel, "Elapsed :").setClassName("training-status");
		formLayout.setSizeFull();
		add(formLayout);
	}

	public void updateTrainingDetailsPanel(Experiment experiment) {
		final var trainingStatus = ExperimentUtils.getTrainingStatus(experiment);
		statusLabel.setText(trainingStatus.toString());

		updateElapsedTimer(experiment, trainingStatus);
		updateProgressRow(experiment, trainingStatus);
	}

	private void updateProgressRow(Experiment experiment, RunStatus trainingStatus) {
		if(trainingStatus == Running) {
			updateProgressBar(experiment);
		} else if (trainingStatus == Completed) {
			DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
				final var trainingCompletedTime = ExperimentUtils.getTrainingCompletedTime(experiment);
				final var formattedTrainingCompletedTime = DateAndTimeUtils.formatDateAndTimeShortFormatter(trainingCompletedTime, userTimeZone);
				progressValueLabel.setText(formattedTrainingCompletedTime);
			});
			progressValueLabel.setVisible(true);
			progressBar.setVisible(false);
		} else {
			progressBar.setVisible(false);
			progressValueLabel.setVisible(false);
		}
	}

	private void updateProgressBar(Experiment experiment) {
		final int totalIterations = RunConstants.PBT_RUN_ITERATIONS;
		final Integer iterationsProcessed = ExperimentUtils.getNumberOfProcessedIterations(experiment);

		final var progress = (iterationsProcessed / totalIterations) * 100;
		if (progress > 0 && progress <= 100) {
			final var estimatedTime = ExperimentUtils.getEstimatedTrainingTime(experiment, progress);
			final var formattedEstimatedTime = DateAndTimeUtils.getOnlyTheHighestDateLevel((long) estimatedTime);
			final var progressValue = formatProgressLabel(progress, formattedEstimatedTime);
			progressValueLabel.setText(progressValue);
			progressBar.setValue(progress);
			progressBar.setVisible(true);
			progressValueLabel.setVisible(true);
		}
	}

	/**
	 * Formats in progress training status.<br/>
	 * Example output: <code>44 % (ETA: 54 min)</code>
	 */
	private String formatProgressLabel(double progress, String formattedEstimatedTime) {
		return String.format("%.0f %% (ETA: %s)", progress, formattedEstimatedTime);
	}

	/**
	 * Calculates a elapsed time and updates a timer. Elapsed time is a difference between training start date and it's
	 * completed date (or current date in case the training is still in progress).
	 */
	private void updateElapsedTimer(Experiment experiment, RunStatus trainingStatus) {
		final var isTrainingRunning = isRunning(trainingStatus);
		final var startTime = ExperimentUtils.getTrainingStartedDate(experiment);
		final var endTime = calculateEndTimeForElapsedTime(experiment, isTrainingRunning);
		final var timeElapsed = Duration.between(startTime, endTime).toSeconds();
		elapsedTimeLabel.updateTimer(timeElapsed, isTrainingRunning);
	}

	private LocalDateTime calculateEndTimeForElapsedTime(Experiment experiment, boolean isTrainingRunning) {
		if (isTrainingRunning) {
			return LocalDateTime.now();
		} else {
			var trainingCompletedTime = ExperimentUtils.getTrainingCompletedTime(experiment);
			return trainingCompletedTime == null ? LocalDateTime.now() : trainingCompletedTime;
		}
	}
}