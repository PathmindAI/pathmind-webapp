package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.ElapsedTimer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindTrainingProgress;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import java.time.Duration;
import java.time.LocalDateTime;

import static io.skymind.pathmind.shared.constants.RunStatus.*;

public class TrainingStatusDetailsPanel extends HorizontalLayout {
	private Span statusLabel = LabelFactory.createLabel(RunStatus.NotStarted.toString());
	/**
	 * Label for training progress status.
	 * If training is still in progress it shows it's % progress. If training is finished it shows its completed date.
	 */
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
	private PathmindTrainingProgress trainingProgress = new PathmindTrainingProgress();
	private Span completedTimeLabel = LabelFactory.createLabel("");
	
	public TrainingStatusDetailsPanel() {
		add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(new Span("Status"), statusLabel, completedTimeLabel),
            WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(new Span("Elapsed"), elapsedTimeLabel),
            trainingProgress);
        addClassName("training-status-details-panel");
        setWidthFull();
		setPadding(false);
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
			getUI().ifPresent(ui -> VaadinDateAndTimeUtils.withUserTimeZoneId(ui, userTimeZone -> {
				LocalDateTime trainingCompletedTime = ExperimentUtils.getTrainingCompletedTime(experiment);
				final var formattedTrainingCompletedTime = DateAndTimeUtils.formatDateAndTimeShortFormatter(trainingCompletedTime, userTimeZone);
				completedTimeLabel.setText(formattedTrainingCompletedTime);
			}));
			completedTimeLabel.setVisible(true);
			trainingProgress.setVisible(false);
		} else {
			trainingProgress.setVisible(false);
			completedTimeLabel.setVisible(false);
		}
	}

	private void updateProgressBar(Experiment experiment) {
		final double progress = ExperimentUtils.calculateProgressByExperiment(experiment);
		if (progress >= 0 && progress <= 100) {
			final double estimatedTime = ExperimentUtils.getEstimatedTrainingTime(experiment, progress);
			trainingProgress.setValue(progress, estimatedTime);
			completedTimeLabel.setVisible(false);
			trainingProgress.setVisible(true);
		}
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