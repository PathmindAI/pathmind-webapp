package io.skymind.pathmind.ui.views.experiment.components;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

import static io.skymind.pathmind.constants.RunStatus.*;

@Component
public class TrainingStatusDetailsPanel extends VerticalLayout {
	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runTypeLabel = new Label();
	private Label progressValueLabel = new Label();
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
	private ProgressBar progressBar = new ProgressBar(0, 100);

	public TrainingStatusDetailsPanel() {
		VerticalLayout progressRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(progressBar, progressValueLabel);
		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
		VerticalLayout statusRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(statusLabel, progressRow);
		formLayout.addFormItem(statusRow, "Status :").setClassName("training-status");
		formLayout.addFormItem(runTypeLabel, "Run Type :").setClassName("training-status");
		formLayout.addFormItem(elapsedTimeLabel, "Elapsed :").setClassName("training-status");
		formLayout.setSizeFull();
		add(formLayout);
	}

	public void updateTrainingDetailsPanel(Experiment experiment) {
		final var trainingStatus = ExperimentUtils.getTrainingStatus(experiment);
		statusLabel.setText(trainingStatus.toString());

		final var runType = ExperimentUtils.getTrainingType(experiment);
		runTypeLabel.setText(runType.toString());

		updateElapsedTimer(experiment, trainingStatus, runType);
		updateProgressRow(experiment, trainingStatus, runType);
	}

	private void updateProgressRow(Experiment experiment, RunStatus trainingStatus, RunType runType) {
		if(trainingStatus == Running) {
			progressBar.setVisible(true);
			progressValueLabel.setVisible(true);
			updateProgressBar(experiment, runType);
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

	private void updateProgressBar(Experiment experiment, RunType runType) {
		final var totalIterations = (double) RunUtils.getNumberOfTrainingIterationsForRunType(runType);
		final Integer iterationsProcessed = ExperimentUtils.getNumberOfProcessedIterations(experiment, runType);

		final var progress = (iterationsProcessed / totalIterations) * 100;
		if (progress > 0 && progress <= 100) {
			final var estimatedTime = ExperimentUtils.getEstimatedTrainingTime(experiment, progress, runType);
			final var formattedEstimatedTime = DateAndTimeUtils.getOnlyTheHighestDateLevel((long) estimatedTime);
			final var progressValue = formatProgressLabel(progress, formattedEstimatedTime);
			progressValueLabel.setText(progressValue);
			progressBar.setValue(progress);
		}
	}

	// TODO (KW): 28.01.2020 javadoc
	private String formatProgressLabel(double progress, String formattedEstimatedTime) {
		return String.format("%.0f %% (ETA: %s)", progress, formattedEstimatedTime);
	}

	private void updateElapsedTimer(Experiment experiment, RunStatus trainingStatus, RunType runType) {
		final var isTrainingRunning = isRunning(trainingStatus);
		final var startTime = ExperimentUtils.getTrainingStartedDate(experiment, runType);
		final var endTime = isTrainingRunning ? LocalDateTime.now() : ExperimentUtils.getTrainingStoppedDate(experiment);
		final var timeElapsed = Duration.between(startTime, endTime).toSeconds();
		elapsedTimeLabel.updateTimer(timeElapsed, isTrainingRunning);
	}
}