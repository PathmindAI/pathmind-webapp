package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.ElapsedTimer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindTrainingProgress;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import static io.skymind.pathmind.shared.constants.RunStatus.isRunning;

public class TrainingStatusDetailsPanel extends HorizontalLayout implements ExperimentComponent {

    private Span statusLabel = LabelFactory.createLabel(RunStatus.NotStarted.toString());
    /**
     * Label for training progress status.
     * If training is still in progress it shows it's % progress. If training is finished it shows its completed date.
     */
    private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
    private PathmindTrainingProgress trainingProgress = new PathmindTrainingProgress();
    private Span completedTimeLabel = LabelFactory.createLabel("");

    private Experiment experiment;

    private Supplier<Optional<UI>> getUISupplier;

    public TrainingStatusDetailsPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        HorizontalLayout statusWrapper = new HorizontalLayout(new Span("Status"), statusLabel, completedTimeLabel);
        statusWrapper.addClassName("status-wrapper");
        statusWrapper.setSpacing(false);
        HorizontalLayout elapsedTimeWrapper = new HorizontalLayout(new Span("Elapsed"), elapsedTimeLabel);
        elapsedTimeWrapper.addClassName("elapsed-time-wrapper");
        elapsedTimeWrapper.setSpacing(false);
        add(statusWrapper, elapsedTimeWrapper, trainingProgress);
        addClassName("training-status-details-panel");
        setWidthFull();
        setPadding(false);
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        update();
    }

    public void update() {
        statusLabel.setText(experiment.getTrainingStatusEnum().toString());
        updateElapsedTimer();
        updateProgressRow();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    private void updateProgressRow() {
        if (RunStatus.isRunning(experiment.getTrainingStatusEnum())) {
            updateProgressBar(experiment);
        } else {
            trainingProgress.setVisible(false);
            completedTimeLabel.setVisible(false);
            getUISupplier.get().ifPresent(ui -> VaadinDateAndTimeUtils.withUserTimeZoneId(ui, userTimeZone -> {
                LocalDateTime trainingCompletedTime = ExperimentUtils.getTrainingCompletedTime(experiment);
                if (Objects.nonNull(trainingCompletedTime)) {
                    completedTimeLabel.setVisible(true);
                    final var formattedTrainingCompletedTime = DateAndTimeUtils.formatDateAndTimeShortFormatter(trainingCompletedTime, userTimeZone);
                    completedTimeLabel.setText(formattedTrainingCompletedTime);
                }
            }));
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
    private void updateElapsedTimer() {
        final var isTrainingRunning = isRunning(experiment.getTrainingStatusEnum());
        final var startTime = ExperimentUtils.getTrainingStartedDate(experiment);
        final var endTime = calculateEndTimeForElapsedTime(isTrainingRunning);
        final var timeElapsed = Duration.between(startTime, endTime).toSeconds();
        elapsedTimeLabel.updateTimer(timeElapsed, isTrainingRunning);
    }

    private LocalDateTime calculateEndTimeForElapsedTime(boolean isTrainingRunning) {
        if (isTrainingRunning) {
            return LocalDateTime.now();
        } else {
            var trainingCompletedTime = ExperimentUtils.getTrainingCompletedTime(experiment);
            return trainingCompletedTime == null ? LocalDateTime.now() : trainingCompletedTime;
        }
    }
}